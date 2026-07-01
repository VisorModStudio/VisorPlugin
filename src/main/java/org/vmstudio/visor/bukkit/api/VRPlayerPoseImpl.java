package org.vmstudio.visor.bukkit.api;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.player.VRPlayerPose;
import org.vmstudio.visor.api.player.VRBodyPartType;
import org.vmstudio.visor.api.player.VRPose;
import org.vmstudio.visor.api.player.VRPoseTrackers;
import org.vmstudio.visor.protocol.value.PoseData;
import org.vmstudio.visor.protocol.value.PoseElement;
import org.vmstudio.visor.protocol.value.Quat;
import org.vmstudio.visor.protocol.value.Vec3f;

public final class VRPlayerPoseImpl implements VRPlayerPose {
    private final Vector origin;
    private final Map<VRBodyPartType, VRPose> poses = new EnumMap<>(VRBodyPartType.class);
    private final VRPose hmd;
    private final VRPose mainHand;
    private final VRPose offhand;
    private final Vector headPivot;
    private final float bodyYaw;
    private final VRPoseTrackers trackers;

    private VRPlayerPoseImpl(){
        this.origin = new Vector(0, 0, 0);
        this.hmd = VRPose.EMPTY;
        this.mainHand = VRPose.EMPTY;
        this.offhand = VRPose.EMPTY;
        this.headPivot = new Vector(0, 0, 0);
        this.bodyYaw = 0f;
        this.trackers = new PoseTrackersImpl(poses);
    }

    private VRPlayerPoseImpl(PoseData pose, Vector origin){
        this.origin = origin;
        VRBodyPartType[] parts = VRBodyPartType.values();

        VRPose h = element(pose.hmd(), origin);
        VRPose m = element(pose.mainHand(), origin);
        VRPose o = element(pose.offhand(), origin);
        this.hmd = h != null ? h : VRPose.EMPTY;
        this.mainHand = m != null ? m : VRPose.EMPTY;
        this.offhand = o != null ? o : VRPose.EMPTY;
        if(h != null){
            poses.put(VRBodyPartType.HEAD, h);
        }
        if(m != null){
            poses.put(VRBodyPartType.MAIN_HAND, m);
        }
        if(o != null){
            poses.put(VRBodyPartType.OFFHAND, o);
        }

        PoseElement[] trackers = pose.trackers().toArray();
        for(int i = 0; i < trackers.length; i++){
            VRPose vp = element(trackers[i], origin);
            if(vp != null){
                poses.put(parts[i + 3], vp);
            }
        }

        this.headPivot = computeHeadPivot(pose.hmd(), origin);
        this.bodyYaw = computeBodyYaw(pose);
        this.trackers = new PoseTrackersImpl(poses);
    }

    public static VRPlayerPose of(PoseData pose, Vector origin){
        return new VRPlayerPoseImpl(pose, origin);
    }

    public static VRPlayerPose empty(){
        return new VRPlayerPoseImpl();
    }

    @Nullable
    private static VRPose element(PoseElement el, Vector origin){
        if(el == null){
            return null;
        }
        Vec3f p = el.position();
        Quat q = el.orientation();
        return new VRPose(
                new Vector(p.x() + origin.getX(), p.y() + origin.getY(), p.z() + origin.getZ()),
                q.x(), q.y(), q.z(), q.w());
    }

    private static Vector computeHeadPivot(PoseElement hmd, Vector origin){
        if(hmd == null){
            return origin.clone();
        }
        Vec3f p = hmd.position();
        Vector t = PoseMath.rotate(hmd.orientation(), 0.0, -0.1, 0.1);
        return new Vector(
                p.x() + origin.getX() + t.getX(),
                p.y() + origin.getY() + t.getY(),
                p.z() + origin.getZ() + t.getZ());
    }

    private static float computeBodyYaw(PoseData pose){
        PoseElement main = pose.mainHand();
        PoseElement off = pose.offhand();
        PoseElement hmd = pose.hmd();
        if(main == null || off == null || hmd == null){
            return 0f;
        }
        Vector body = new Vector(
                off.position().x() - main.position().x(),
                off.position().y() - main.position().y(),
                off.position().z() - main.position().z());
        if(body.lengthSquared() < 1e-12){
            return 0f;
        }
        body.normalize();
        body = PoseMath.rotateY(body, -Math.PI / 2.0);
        Vector hmdDir = PoseMath.rotate(hmd.orientation(), 0.0, 0.0, -1.0);
        if(body.dot(hmdDir) < 0.0){
            body.multiply(-1.0);
        }
        Vector lerped = new Vector(
                hmdDir.getX() + (body.getX() - hmdDir.getX()) * 0.7,
                hmdDir.getY() + (body.getY() - hmdDir.getY()) * 0.7,
                hmdDir.getZ() + (body.getZ() - hmdDir.getZ()) * 0.7);
        return (float) Math.atan2(-lerped.getX(), lerped.getZ());
    }

    @Override
    public @NotNull VRPose getHmd(){
        return hmd;
    }

    @Override
    public @NotNull VRPose getMainHand(){
        return mainHand;
    }

    @Override
    public @NotNull VRPose getOffhand(){
        return offhand;
    }

    @Override
    public @Nullable VRPose getPose(@NotNull VRBodyPartType bodyPart){
        return poses.get(bodyPart);
    }

    @Override
    public @NotNull VRPoseTrackers getTrackers(){
        return trackers;
    }

    @Override
    public @NotNull Vector getOrigin(){
        return origin.clone();
    }

    @Override
    public float getWorldScale(){
        return 1.0f;
    }

    @Override
    public float getRotationY(){
        return 0f;
    }

    @Override
    public @NotNull Vector getHeadPivot(){
        return headPivot.clone();
    }

    @Override
    public float getBodyYaw(){
        return bodyYaw;
    }
}

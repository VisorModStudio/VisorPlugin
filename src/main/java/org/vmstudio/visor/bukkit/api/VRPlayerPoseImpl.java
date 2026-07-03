package org.vmstudio.visor.bukkit.api;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vmstudio.visor.api.player.VRPlayerPose;
import org.vmstudio.visor.api.player.VRBodyPartType;
import org.vmstudio.visor.api.player.VRPose;
import org.vmstudio.visor.api.player.VRPoseTrackers;
import org.vmstudio.visor.protocol.value.PoseData;
import org.vmstudio.visor.protocol.value.PoseElement;
import org.vmstudio.visor.protocol.value.Quat;
import org.vmstudio.visor.protocol.value.Vec3f;

public final class VRPlayerPoseImpl implements VRPlayerPose {
    private final Vector3fc origin;
    private final Map<VRBodyPartType, VRPose> poses = new EnumMap<>(VRBodyPartType.class);
    private final VRPose hmd;
    private final VRPose mainHand;
    private final VRPose offhand;
    private final Vector3fc headPivot;
    private final float bodyYaw;
    private final VRPoseTrackers trackers;

    private VRPlayerPoseImpl(){
        this.origin = new Vector3f(0, 0, 0);
        this.hmd = VRPose.EMPTY;
        this.mainHand = VRPose.EMPTY;
        this.offhand = VRPose.EMPTY;
        this.headPivot = new Vector3f(0, 0, 0);
        this.bodyYaw = 0f;
        this.trackers = new PoseTrackersImpl(poses);
    }

    private VRPlayerPoseImpl(PoseData pose, Vector3fc origin){
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

        PoseElement[] trackerElements = pose.trackers().toArray();
        for(int i = 0; i < trackerElements.length; i++){
            VRPose vp = element(trackerElements[i], origin);
            if(vp != null){
                poses.put(parts[i + 3], vp);
            }
        }

        this.headPivot = computeHeadPivot(pose.hmd(), origin);
        this.bodyYaw = computeBodyYaw(pose);
        this.trackers = new PoseTrackersImpl(poses);
    }

    public static VRPlayerPose of(PoseData pose, Vector3fc origin){
        return new VRPlayerPoseImpl(pose, origin);
    }

    public static VRPlayerPose empty(){
        return new VRPlayerPoseImpl();
    }

    @Nullable
    private static VRPose element(PoseElement el, Vector3fc origin){
        if(el == null){
            return null;
        }
        Vec3f p = el.position();
        Quat q = el.orientation();
        return new VRPose(
                new Vector3f(p.x() + origin.x(), p.y() + origin.y(), p.z() + origin.z()),
                new Quaternionf(q.x(), q.y(), q.z(), q.w()));
    }

    private static Vector3fc computeHeadPivot(PoseElement hmd, Vector3fc origin){
        if(hmd == null){
            return new Vector3f(origin);
        }
        Vec3f p = hmd.position();
        Quat q = hmd.orientation();
        Vector3f t = new Quaternionf(q.x(), q.y(), q.z(), q.w()).transform(new Vector3f(0f, -0.1f, 0.1f));
        return new Vector3f(
                p.x() + origin.x() + t.x,
                p.y() + origin.y() + t.y,
                p.z() + origin.z() + t.z);
    }

    private static float computeBodyYaw(PoseData pose){
        PoseElement main = pose.mainHand();
        PoseElement off = pose.offhand();
        PoseElement hmd = pose.hmd();
        if(main == null || off == null || hmd == null){
            return 0f;
        }
        Vector3f body = new Vector3f(
                off.position().x() - main.position().x(),
                off.position().y() - main.position().y(),
                off.position().z() - main.position().z());
        if(body.lengthSquared() < 1e-12f){
            return 0f;
        }
        body.normalize();
        body.rotateY((float) (-Math.PI / 2.0));
        Quat hq = hmd.orientation();
        Vector3f hmdDir = new Quaternionf(hq.x(), hq.y(), hq.z(), hq.w()).transform(new Vector3f(0f, 0f, -1f));
        if(body.dot(hmdDir) < 0f){
            body.mul(-1f);
        }
        Vector3f lerped = new Vector3f(
                hmdDir.x + (body.x - hmdDir.x) * 0.7f,
                hmdDir.y + (body.y - hmdDir.y) * 0.7f,
                hmdDir.z + (body.z - hmdDir.z) * 0.7f);
        return (float) Math.atan2(-lerped.x, lerped.z);
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
    public @NotNull Vector3fc getOrigin(){
        return origin;
    }

    @Override
    public @NotNull Vector getOriginMc(){
        return toMc(origin);
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
    public @NotNull Vector3fc getHeadPivot(){
        return headPivot;
    }

    @Override
    public @NotNull Vector getHeadPivotMc(){
        return toMc(headPivot);
    }

    @Override
    public float getBodyYaw(){
        return bodyYaw;
    }

    private static Vector toMc(Vector3fc v){
        return new Vector(v.x(), v.y(), v.z());
    }
}

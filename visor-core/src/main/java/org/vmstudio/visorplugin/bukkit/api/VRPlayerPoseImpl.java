package org.vmstudio.visorplugin.bukkit.api;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vmstudio.visorplugin.api.player.VRPlayerPose;
import org.vmstudio.visorplugin.api.player.VRBodyPartType;
import org.vmstudio.visorplugin.api.player.VRPose;
import org.vmstudio.visorplugin.api.player.VRPoseHands;
import org.vmstudio.visorplugin.api.player.VRPoseTrackers;
import org.vmstudio.visorplugin.api.network.buffer.PoseDataBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseElementBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseHandBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseHandJointBuffer;


public final class VRPlayerPoseImpl implements VRPlayerPose {
    private final Vector3fc origin;
    private final Map<VRBodyPartType, VRPose> poses = new EnumMap<>(VRBodyPartType.class);
    private final VRPose hmd;
    private final VRPose mainHand;
    private final VRPose offhand;
    private final Vector3fc headPivot;
    private final float bodyYaw;
    private final float rotationY;
    private final VRPoseTrackers trackers;
    private final VRPoseHands hands;

    private VRPlayerPoseImpl(){
        this.origin = new Vector3f(0, 0, 0);
        this.hmd = VRPose.EMPTY;
        this.mainHand = VRPose.EMPTY;
        this.offhand = VRPose.EMPTY;
        this.headPivot = new Vector3f(0, 0, 0);
        this.bodyYaw = 0f;
        this.rotationY = 0f;
        this.trackers = new PoseTrackersImpl(poses);
        this.hands = VRPoseHands.EMPTY;
    }

    private VRPlayerPoseImpl(PoseDataBuffer pose, Vector3fc origin, float turnRotationY, boolean roomRelative){
        this.origin = origin;
        this.rotationY = roomRelative ? 0f : turnRotationY;

       float phi = this.rotationY - turnRotationY;
        VRBodyPartType[] parts = VRBodyPartType.values();

        PoseElementBuffer hmdEl = rot(pose.hmd(), phi);
        PoseElementBuffer mainEl = rot(pose.mainHand(), phi);
        PoseElementBuffer offEl = rot(pose.offhand(), phi);

        VRPose h = element(hmdEl, origin);
        VRPose m = element(mainEl, origin);
        VRPose o = element(offEl, origin);
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

        PoseElementBuffer[] trackerElements = pose.trackers().toArray();
        for(int i = 0; i < trackerElements.length; i++){
            VRPose vp = element(rot(trackerElements[i], phi), origin);
            if(vp != null){
                poses.put(parts[i + 3], vp);
            }
        }

        this.headPivot = computeHeadPivot(hmdEl, origin);
        this.bodyYaw = computeBodyYaw(hmdEl, mainEl, offEl);
        this.trackers = new PoseTrackersImpl(poses);
        this.hands = new PoseHandsImpl(
                hand(pose.hands().leftHand(), phi, origin),
                hand(pose.hands().rightHand(), phi, origin));
    }

    public static VRPlayerPose of(PoseDataBuffer pose, Vector3fc origin, float turnRotationY, boolean roomRelative){
        return new VRPlayerPoseImpl(pose, origin, turnRotationY, roomRelative);
    }

    public static VRPlayerPose empty(){
        return new VRPlayerPoseImpl();
    }


    @Nullable
    private static PoseElementBuffer rot(@Nullable PoseElementBuffer el, float phi){
        if(el == null || phi == 0f){
            return el;
        }
        Vector3f p = new Vector3f(el.position()).rotateY(phi);
        Quaternionf q = new Quaternionf().rotationY(phi).mul(el.orientation());
        return new PoseElementBuffer(el.type(), p, q);
    }

    private static PoseHandsImpl.PoseHandImpl hand(PoseHandBuffer buf, float phi, Vector3fc origin){
        PoseHandsImpl.PoseHandImpl out = new PoseHandsImpl.PoseHandImpl();
        for(PoseHandJointBuffer joint : buf.joints()){
            Vector3fc p = joint.position();
            Quaternionfc q = joint.orientation();
            if(phi != 0f){
                p = new Vector3f(p).rotateY(phi);
                q = new Quaternionf().rotationY(phi).mul(q);
            }
            out.put(joint.type(), new VRPose(
                    new Vector3f(p.x() + origin.x(), p.y() + origin.y(), p.z() + origin.z()),
                    new Quaternionf(q.x(), q.y(), q.z(), q.w())));
        }
        return out;
    }

    @Nullable
    private static VRPose element(PoseElementBuffer el, Vector3fc origin){
        if(el == null){
            return null;
        }
        Vector3fc p = el.position();
        Quaternionfc q = el.orientation();
        return new VRPose(
                new Vector3f(p.x() + origin.x(), p.y() + origin.y(), p.z() + origin.z()),
                new Quaternionf(q.x(), q.y(), q.z(), q.w()));
    }

    private static Vector3fc computeHeadPivot(PoseElementBuffer hmd, Vector3fc origin){
        if(hmd == null){
            return new Vector3f(origin);
        }
        Vector3fc p = hmd.position();
        Quaternionfc q = hmd.orientation();
        Vector3f t = new Quaternionf(q.x(), q.y(), q.z(), q.w()).transform(new Vector3f(0f, -0.1f, 0.1f));
        return new Vector3f(
                p.x() + origin.x() + t.x,
                p.y() + origin.y() + t.y,
                p.z() + origin.z() + t.z);
    }

    private static float computeBodyYaw(PoseElementBuffer hmd, PoseElementBuffer main, PoseElementBuffer off){
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
        Quaternionfc hq = hmd.orientation();
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
    public @NotNull VRPoseHands getHands(){
        return hands;
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
        return rotationY;
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

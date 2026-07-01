package org.vmstudio.visor.bukkit.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.player.HandType;
import org.vmstudio.visor.api.player.VRBodyPartType;
import org.vmstudio.visor.api.player.VRPlayer;
import org.vmstudio.visor.api.player.VRPlayerPose;
import org.vmstudio.visor.api.player.VRPose;
import org.vmstudio.visor.api.player.VRPoseHistory;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visor.common.session.PoseHistoryBuffer;
import org.vmstudio.visor.common.session.VisorSession;

public final class VRPlayerImpl extends VisorPlayerImpl implements VRPlayer {
    public VRPlayerImpl(VisorSession session, Player player){
        super(session, player);
    }

    @Override
    public @NotNull VRPlayerPose getPoseData(){
        return poseAt(0, true);
    }

    @Override
    public @NotNull VRPlayerPose getPoseDataPrevious(){
        List<PoseHistoryBuffer.Snapshot> h = session.vr().getPoseHistory();
        return poseFrom(h, h.size() > 1 ? 1 : 0, true);
    }

    @Override
    public @NotNull VRPlayerPose getPoseDataRelative(){
        return poseAt(0, false);
    }

    @Override
    public @NotNull VRPoseHistory getPoseHistoryRelative(){
        return history(false);
    }

    @Override
    public @NotNull VRPoseHistory getPoseHistoryTick(){
        return history(true);
    }

    private VRPlayerPose poseAt(int idx, boolean worldSpace){
        return poseFrom(session.vr().getPoseHistory(), idx, worldSpace);
    }

    private static VRPlayerPose poseFrom(List<PoseHistoryBuffer.Snapshot> h, int idx, boolean worldSpace){
        if(h.isEmpty() || idx >= h.size()){
            return VRPlayerPoseImpl.empty();
        }
        PoseHistoryBuffer.Snapshot s = h.get(idx);
        return VRPlayerPoseImpl.of(s.pose(), originOf(s, worldSpace));
    }

    private VRPoseHistory history(boolean worldSpace){
        List<PoseHistoryBuffer.Snapshot> h = session.vr().getPoseHistory();
        List<VRPlayerPose> list = new ArrayList<>(h.size());
        for(PoseHistoryBuffer.Snapshot s : h){
            list.add(VRPlayerPoseImpl.of(s.pose(), originOf(s, worldSpace)));
        }
        return new PoseHistoryImpl(list);
    }

    private static Vector originOf(PoseHistoryBuffer.Snapshot s, boolean worldSpace){
        return worldSpace ? new Vector(s.ox(), s.oy(), s.oz()) : new Vector(0, 0, 0);
    }

    @Override
    public boolean isLeftHanded(){
        return session.vr().isLeftHanded();
    }

    @Override
    public @NotNull HandType getActiveHand(){
        return session.vr().isActiveHandMain() ? HandType.MAIN : HandType.OFFHAND;
    }

    @Override
    public int getOffhandSlot(){
        return session.vr().getOffhandSlot();
    }

    @Override
    public float getWorldScale(){
        return session.vr().getWorldScale();
    }

    @Override
    public float getFullHeight(){
        return session.vr().getFullHeight();
    }

    @Override
    public float getGunAngle(){
        return session.vr().getGunAngle();
    }

    @Override
    public float getRotationY(){
        return session.vr().getRotationY();
    }

    @Override
    public boolean isOverlayFocused(){
        return session.vr().isOverlayFocused();
    }

    @Override
    public boolean isCrawling(){
        return session.vr().isCrawling();
    }

    @Override
    public String getBodyType(){
        return session.vr().getBodyType();
    }

    public static @Nullable VRPlayer fromVisorSession(VisorSession session){
        if(session == null || !session.vrActive()){
            return null;
        }
        Player player = ((BukkitPlatformPlayer) session.player()).handle();
        return new VRPlayerImpl(session, player);
    }
}

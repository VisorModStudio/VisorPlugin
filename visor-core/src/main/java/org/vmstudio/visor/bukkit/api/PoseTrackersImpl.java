package org.vmstudio.visor.bukkit.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.player.VRBodyPartType;
import org.vmstudio.visor.api.player.VRPose;
import org.vmstudio.visor.api.player.VRPoseTrackers;

public final class PoseTrackersImpl implements VRPoseTrackers {
    private static final VRBodyPartType[] TRACKER_PARTS = {
            VRBodyPartType.WAIST, VRBodyPartType.CHEST,
            VRBodyPartType.LEFT_FOOT, VRBodyPartType.RIGHT_FOOT,
            VRBodyPartType.LEFT_ANKLE, VRBodyPartType.RIGHT_ANKLE,
            VRBodyPartType.LEFT_KNEE, VRBodyPartType.RIGHT_KNEE,
            VRBodyPartType.LEFT_WRIST, VRBodyPartType.RIGHT_WRIST,
            VRBodyPartType.LEFT_ELBOW, VRBodyPartType.RIGHT_ELBOW,
            VRBodyPartType.LEFT_SHOULDER, VRBodyPartType.RIGHT_SHOULDER,
    };

    private final Map<VRBodyPartType, VRPose> poses;
    private final List<VRPose> activePose = new ArrayList<>();
    private final List<VRBodyPartType> activeType = new ArrayList<>();

    public PoseTrackersImpl(Map<VRBodyPartType, VRPose> poses){
        this.poses = poses;
        for(VRBodyPartType part : TRACKER_PARTS){
            VRPose p = poses.get(part);
            if(p != null){
                activePose.add(p);
                activeType.add(part);
            }
        }
    }

    @Override
    public boolean isActive(){
        return !activePose.isEmpty();
    }

    @Override
    public List<VRPose> getActiveTrackersPose(){
        return List.copyOf(activePose);
    }

    @Override
    public List<VRBodyPartType> getActiveTrackersType(){
        return List.copyOf(activeType);
    }

    @Override
    public @Nullable VRPose getWaist(){
        return poses.get(VRBodyPartType.WAIST);
    }

    @Override
    public @Nullable VRPose getChest(){
        return poses.get(VRBodyPartType.CHEST);
    }

    @Override
    public @Nullable VRPose getLeftFoot(){
        return poses.get(VRBodyPartType.LEFT_FOOT);
    }

    @Override
    public @Nullable VRPose getRightFoot(){
        return poses.get(VRBodyPartType.RIGHT_FOOT);
    }

    @Override
    public @Nullable VRPose getLeftAnkle(){
        return poses.get(VRBodyPartType.LEFT_ANKLE);
    }

    @Override
    public @Nullable VRPose getRightAnkle(){
        return poses.get(VRBodyPartType.RIGHT_ANKLE);
    }

    @Override
    public @Nullable VRPose getLeftKnee(){
        return poses.get(VRBodyPartType.LEFT_KNEE);
    }

    @Override
    public @Nullable VRPose getRightKnee(){
        return poses.get(VRBodyPartType.RIGHT_KNEE);
    }

    @Override
    public @Nullable VRPose getLeftWrist(){
        return poses.get(VRBodyPartType.LEFT_WRIST);
    }

    @Override
    public @Nullable VRPose getRightWrist(){
        return poses.get(VRBodyPartType.RIGHT_WRIST);
    }

    @Override
    public @Nullable VRPose getLeftElbow(){
        return poses.get(VRBodyPartType.LEFT_ELBOW);
    }

    @Override
    public @Nullable VRPose getRightElbow(){
        return poses.get(VRBodyPartType.RIGHT_ELBOW);
    }

    @Override
    public @Nullable VRPose getLeftShoulder(){
        return poses.get(VRBodyPartType.LEFT_SHOULDER);
    }

    @Override
    public @Nullable VRPose getRightShoulder(){
        return poses.get(VRBodyPartType.RIGHT_SHOULDER);
    }
}

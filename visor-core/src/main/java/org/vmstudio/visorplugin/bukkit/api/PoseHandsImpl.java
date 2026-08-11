package org.vmstudio.visorplugin.bukkit.api;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visorplugin.api.player.VRHandJointType;
import org.vmstudio.visorplugin.api.player.VRPose;
import org.vmstudio.visorplugin.api.player.VRPoseHand;
import org.vmstudio.visorplugin.api.player.VRPoseHands;

public final class PoseHandsImpl implements VRPoseHands {
    private final PoseHandImpl leftHand;
    private final PoseHandImpl rightHand;

    public PoseHandsImpl(PoseHandImpl leftHand, PoseHandImpl rightHand){
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    @Override
    public boolean isActive(){
        return leftHand.isActive() || rightHand.isActive();
    }

    @Override
    public @NotNull VRPoseHand getLeftHand(){
        return leftHand;
    }

    @Override
    public @NotNull VRPoseHand getRightHand(){
        return rightHand;
    }

    public static final class PoseHandImpl implements VRPoseHand {
        private final Map<VRHandJointType, VRPose> joints = new EnumMap<>(VRHandJointType.class);
        private final List<VRPose> activePose = new ArrayList<>();
        private final List<VRHandJointType> activeType = new ArrayList<>();

        public void put(VRHandJointType type, VRPose pose){
            joints.put(type, pose);
            activePose.add(pose);
            activeType.add(type);
        }

        @Override
        public boolean isActive(){
            return !activePose.isEmpty();
        }

        @Override
        public @NotNull List<VRPose> getActiveJointsPose(){
            return List.copyOf(activePose);
        }

        @Override
        public @NotNull List<VRHandJointType> getActiveJointsType(){
            return List.copyOf(activeType);
        }

        @Override
        public @Nullable VRPose getJoint(@NotNull VRHandJointType joint){
            return joints.get(joint);
        }
    }
}

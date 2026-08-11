package org.vmstudio.visorplugin.api.player;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Hand skeleton pose of a single hand.
 * <p>
 *   Only populated when the player sends hand tracking data,
 *   which requires {@code handTrackersSupported} on the server
 *   and hand tracking enabled on the client.
 * </p>
 */
public interface VRPoseHand {

    VRPoseHand EMPTY = new VRPoseHand() {
        @Override public boolean isActive() {return false;}
        @Override public @NotNull List<VRPose> getActiveJointsPose() {return List.of();}
        @Override public @NotNull List<VRHandJointType> getActiveJointsType() {return List.of();}
        @Override public @Nullable VRPose getJoint(@NotNull VRHandJointType joint) {return null;}
    };

    boolean isActive();

    @NotNull List<VRPose> getActiveJointsPose();

    @NotNull List<VRHandJointType> getActiveJointsType();

    @Nullable VRPose getJoint(@NotNull VRHandJointType joint);

}

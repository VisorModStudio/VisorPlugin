package org.vmstudio.visorplugin.api.player;

import org.jetbrains.annotations.NotNull;

/**
 * Hand skeleton pose of both hands.
 * <p>
 *   The sides here are physical ones,
 *   they are not affected by {@link VRPlayer#isLeftHanded()}
 * </p>
 */
public interface VRPoseHands {

    VRPoseHands EMPTY = new VRPoseHands() {
        @Override public boolean isActive() {return false;}
        @Override public @NotNull VRPoseHand getLeftHand() {return VRPoseHand.EMPTY;}
        @Override public @NotNull VRPoseHand getRightHand() {return VRPoseHand.EMPTY;}
    };

    boolean isActive();

    @NotNull VRPoseHand getLeftHand();

    @NotNull VRPoseHand getRightHand();

}

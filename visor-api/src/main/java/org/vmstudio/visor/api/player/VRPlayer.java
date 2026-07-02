package org.vmstudio.visor.api.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface for VR players
 * A {@link VisorPlayer} who is currently in VR
 */
public interface VRPlayer extends VisorPlayer {
    float DEFAULT_GUN_ANGLE = 60.0F;
    float DEFAULT_FULL_HEIGHT = 1.52f;

    @NotNull
    VRPlayerPose getPoseDataPrevious();

    @NotNull
    VRPlayerPose getPoseDataRelative();

    @NotNull
    VRPlayerPose getPoseData();

    /**
     * get pose history for relative type
     *
     * @return pose history
     */
    @NotNull
    VRPoseHistory getPoseHistoryRelative();

    /**
     * get pose history for tick type
     *
     * @return pose history
     */
    @NotNull
    VRPoseHistory getPoseHistoryTick();

    /**
     * The gun angle is used for item pose
     * compatibility with different controllers.
     *
     * @return the gun angle
     */
    float getGunAngle();

    int getOffhandSlot();

    /**
     * If this VR player is left-handed
     *
     * @return true/false
     */
    boolean isLeftHanded();

    /**
     * Get hand type which is currently used
     * by player for attack/mining
     *
     * @return hand type
     */
    @NotNull
    HandType getActiveHand();

    /**
     * Get full height
     *
     * @return full height
     */
    float getFullHeight();

    /**
     * Get actual height
     *
     * @return actual height
     */
    default float getActualHeight(){
        return (float) getPoseDataRelative().getHeadPivot().getY();
    }

    /**
     * Get full height scale.
     * <p>
     *     It is the ratio between {@link #getFullHeight()}
     *     and height of a minecraft player
     * </p>
     *
     * @return full height scale
     */
    default float getFullHeightScale(){
        return getFullHeight() / DEFAULT_FULL_HEIGHT;
    }

    /**
     * Whether the player currently has an overlay
     *
     * @return true when an overlay is active
     */
    boolean isOverlayFocused();

    float getWorldScale();

    float getRotationY();

    boolean isCrawling();

    String getBodyType();
}

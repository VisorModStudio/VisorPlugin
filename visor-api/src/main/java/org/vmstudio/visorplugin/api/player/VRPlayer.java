package org.vmstudio.visorplugin.api.player;

import org.jetbrains.annotations.NotNull;

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
    VRPlayerPose getPoseDataRoom();

    @NotNull
    VRPlayerPose getPoseData();

    /**
     * get pose history for room type
     *
     * @return pose history
     */
    @NotNull
    VRPoseHistory getPoseHistoryRoom();

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

    /**
     * Get offhand slot (if twoHanded gameplay is active)
     * IGNORE, NOT SUPPORTED YET
     * @return 0..9
     */
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
        return getPoseDataRoom().getHeadPivot().y();
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

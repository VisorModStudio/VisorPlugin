package org.vmstudio.visor.api.player;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides pose data for VR player
 */
public interface VRPlayerPose {
    /**
     * Get the head-mounted display (HMD) pose.
     *
     * @return the HMD pose
     */
    @NotNull
    VRPose getHmd();

    /**
     * Get the main hand pose.
     * <p>
     *   This represents the aiming pose of VR controller.
     * </p>
     *
     * @return the main hand pose
     */
    @NotNull
    VRPose getMainHand();

    /**
     * Get the offhand pose.
     * <p>
     *   This represents the aiming pose of VR controller.
     * </p>
     *
     * @return the offhand pose
     */
    @NotNull
    VRPose getOffhand();

    /**
     * Get the hand pose for the given hand type.
     * <p>
     *   This represents the aiming pose of VR controller
     * </p>
     *
     * @param handType the hand type
     * @return the hand pose
     */
    @NotNull
    default VRPose getHand(@NotNull HandType handType){
        return handType == HandType.MAIN ? getMainHand() : getOffhand();
    }

    /**
     * Get pose from body part
     *
     * @param bodyPart the body part
     * @return pose
     */
    @Nullable
    VRPose getPose(@NotNull VRBodyPartType bodyPart);

    /**
     * Get trackers pose
     *
     * @return the trackers pose
     */
    @NotNull
    VRPoseTrackers getTrackers();

    /**
     * Get the origin to which pose data is relative to.
     * <p>
     *   The origin is used by Visor to convert
     *   pose data in VR room coordinates
     *   to world coordinates relative to player
     * </p>
     *
     * @return the origin
     */
    @NotNull
    Vector getOrigin();

    /**
     * Get the world scale.
     * <p>
     *   This defines how VR units map to world units.
     * </p>
     *
     * (Always {@code 1.0} because server-side)
     * @return the world scale factor
     */
    float getWorldScale();

    /**
     * Get the player rotation Y in radians.
     * <p>
     *   This is the general yaw rotation for VR.
     *   We don't use the yaw from minecraft
     *   to have a protection layer from motion sickness
     *   between VR and minecraft logic made for PCs.
     *   The other reason is to be able to control
     *   player model/ray tracing without interfering VR
     * </p>
     *
     * (Always {@code 0} because server-side)
     * @return the player's rotation Y in radians
     */
    float getRotationY();

    /**
     * Get the head pivot point.
     * <p>
     *   This is an approximate point around which the head rotates,
     *   i.e. the position of the player's neck.
     * </p>
     *
     * @return the head pivot position
     */
    @NotNull
    Vector getHeadPivot();

    /**
     * Get the body yaw in radians.
     * <p>
     *   This is the rotation of the body around the Y axis,
     *   which may differ from the head rotation.
     * </p>
     *
     * @return the body yaw in radians
     */
    float getBodyYaw();
}
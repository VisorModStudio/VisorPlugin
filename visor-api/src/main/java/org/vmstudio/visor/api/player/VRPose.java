package org.vmstudio.visor.api.player;

import org.bukkit.util.Vector;

/**
 * Represents a VR spatial data (controller, hmd, eyes, body parts etc.)
 */
public record VRPose(Vector offset, float qx, float qy, float qz, float qw){
    /**
     * An empty element with zero position, zero direction,
     * and identity rotation.
     */
    public static final VRPose EMPTY = new VRPose(new Vector(0, 0, 0), 0, 0, 0, 1);
}

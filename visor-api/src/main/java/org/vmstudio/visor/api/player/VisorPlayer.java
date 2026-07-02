package org.vmstudio.visor.api.player;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Player with Visor mod installed (VR or NonVR)
 */
public interface VisorPlayer {
    UUID getUuid();
    Player getMcPlayer();
    String getClientVersion();

    default boolean isVR(){
        return asVR() != null;
    }
    default boolean isNonVR(){
        return asVR() == null;
    }

    @Nullable
    default VRPlayer asVR(){
        return this instanceof VRPlayer vr ? vr : null;
    }
}

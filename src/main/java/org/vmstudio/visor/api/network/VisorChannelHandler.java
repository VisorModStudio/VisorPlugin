package org.vmstudio.visor.api.network;

import org.bukkit.entity.Player;

/**
 * Receives raw payloads a client sent on a registered {@link VisorChannel}
 */
@FunctionalInterface
public interface VisorChannelHandler {
    void onMessage(Player sender, byte[] payload);
}
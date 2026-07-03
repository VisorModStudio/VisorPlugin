package org.vmstudio.visor.api.network;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


@FunctionalInterface
public interface VisorChannelHandler {
    void onMessage(@NotNull Player sender, byte[] payload);
}
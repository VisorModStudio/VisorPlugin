package org.vmstudio.visor.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformServer;
import org.vmstudio.visor.common.VisorServer;
import org.vmstudio.visor.protocol.VisorProtocol;

public final class VisorChannelListener implements PluginMessageListener {
    private final BukkitPlatformServer platform;
    private final VisorServer visor;

    public VisorChannelListener(BukkitPlatformServer platform, VisorServer visor){
        this.platform = platform;
        this.visor = visor;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message){
        if(!VisorProtocol.CHANNEL.equals(channel)){
            return;
        }
        visor.onMessage(platform.wrap(player), message);
    }
}

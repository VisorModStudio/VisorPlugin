package org.vmstudio.visor.bukkit.listener;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformServer;
import org.vmstudio.visor.common.VisorServer;

public final class VisorLifecycleListener implements Listener {
    private final BukkitPlatformServer platform;
    private final VisorServer visor;

    public VisorLifecycleListener(BukkitPlatformServer platform, VisorServer visor){
        this.platform = platform;
        this.visor = visor;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        visor.onJoin(platform.wrap(event.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        visor.onQuit(uuid);
        platform.adapter().onPlayerQuit(uuid);
    }
}

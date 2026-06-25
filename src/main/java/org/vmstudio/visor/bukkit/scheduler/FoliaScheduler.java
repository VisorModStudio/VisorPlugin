package org.vmstudio.visor.bukkit.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visor.common.platform.Cancellable;
import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.platform.Scheduler;

public final class FoliaScheduler implements Scheduler {
    private final Plugin plugin;

    public FoliaScheduler(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public Cancellable runGlobalTimer(Runnable task, long periodTicks){
        ScheduledTask handle = Bukkit.getGlobalRegionScheduler()
                .runAtFixedRate(plugin, t -> task.run(), 1L, Math.max(1L, periodTicks));
        return handle::cancel;
    }

    @Override
    public void runForPlayer(PlatformPlayer player, Runnable task){
        Player handle = ((BukkitPlatformPlayer) player).handle();
        handle.getScheduler().run(plugin, t -> task.run(), null);
    }

    @Override
    public void runForPlayerDelayed(PlatformPlayer player, Runnable task, long delayTicks){
        Player handle = ((BukkitPlatformPlayer) player).handle();
        handle.getScheduler().runDelayed(plugin, t -> task.run(), null, Math.max(1L, delayTicks));
    }
}

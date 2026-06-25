package org.vmstudio.visor.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.vmstudio.visor.common.platform.Cancellable;
import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.platform.Scheduler;

public final class MainThreadScheduler implements Scheduler {
    private final Plugin plugin;

    public MainThreadScheduler(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public Cancellable runGlobalTimer(Runnable task, long periodTicks){
        BukkitTask handle = Bukkit.getScheduler().runTaskTimer(plugin, task, 0L, periodTicks);
        return handle::cancel;
    }

    @Override
    public void runForPlayer(PlatformPlayer player, Runnable task){
        task.run();
    }

    @Override
    public void runForPlayerDelayed(PlatformPlayer player, Runnable task, long delayTicks){
        Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }
}

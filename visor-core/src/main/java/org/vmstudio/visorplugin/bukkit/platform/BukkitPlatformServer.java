package org.vmstudio.visorplugin.bukkit.platform;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.vmstudio.visorplugin.bukkit.ServerCore;
import org.vmstudio.visorplugin.bukkit.scheduler.FoliaScheduler;
import org.vmstudio.visorplugin.bukkit.scheduler.MainThreadScheduler;
import org.vmstudio.visorplugin.common.platform.PlatformPlayer;
import org.vmstudio.visorplugin.common.platform.PlatformServer;
import org.vmstudio.visorplugin.common.platform.Scheduler;
import org.vmstudio.visorplugin.common.platform.VisorLogger;
import org.vmstudio.visorplugin.api.nms.McVersion;
import org.vmstudio.visorplugin.bukkit.adapter.VisorVersionAdapter;

public final class BukkitPlatformServer implements PlatformServer {
    private final Plugin plugin;
    private final VisorVersionAdapter adapter;
    private final McVersion mcVersion;
    private final ServerCore core;
    private final VisorLogger logger;
    private final Scheduler scheduler;

    public BukkitPlatformServer(Plugin plugin, VisorVersionAdapter adapter, McVersion mcVersion,
                                ServerCore core, VisorLogger logger){
        this.plugin = plugin;
        this.adapter = adapter;
        this.mcVersion = mcVersion;
        this.core = core;
        this.logger = logger;
        this.scheduler = core.isFolia() ? new FoliaScheduler(plugin) : new MainThreadScheduler(plugin);
    }

    public Plugin plugin(){
        return plugin;
    }

    public VisorVersionAdapter adapter(){
        return adapter;
    }

    public PlatformPlayer wrap(Player player){
        return new BukkitPlatformPlayer(player, this);
    }

    @Override
    public String minecraftVersion(){
        return mcVersion.raw();
    }

    @Override
    public boolean isPaper(){
        return core.isPaper();
    }

    @Override
    public boolean isFolia(){
        return core.isFolia();
    }

    @Override
    public Optional<PlatformPlayer> player(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? Optional.empty() : Optional.of(wrap(player));
    }

    @Override
    public Collection<PlatformPlayer> onlinePlayers(){
        List<PlatformPlayer> out = Bukkit.getOnlinePlayers().stream()
                .map(this::wrap)
                .collect(Collectors.toList());
        return out;
    }

    @Override
    public Scheduler scheduler(){
        return scheduler;
    }

    @Override
    public VisorLogger logger(){
        return logger;
    }
}

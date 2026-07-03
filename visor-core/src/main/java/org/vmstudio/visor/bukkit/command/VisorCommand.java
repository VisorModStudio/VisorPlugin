package org.vmstudio.visor.bukkit.command;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.bukkit.logger.BukkitLogger;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visor.common.VisorServer;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.common.settings.SettingsLoader;
import org.vmstudio.visor.common.settings.VisorSettings;

public final class VisorCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "visor.admin";

    private final VisorServer visor;
    private final Path settingsFile;
    private final BukkitLogger logger;

    public VisorCommand(VisorServer visor, Path settingsFile, BukkitLogger logger){
        this.visor = visor;
        this.settingsFile = settingsFile;
        this.logger = logger;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String[] args){
        if(args.length == 0){
            sender.sendMessage("§bVisor §7- /visor reload | status | trackers");
            return true;
        }
        switch(args[0].toLowerCase(Locale.ROOT)){
            case "reload" -> {
                if(!sender.hasPermission(PERMISSION)){
                    sender.sendMessage("§cYou don't have permission.");
                    return true;
                }
                VisorSettings settings = SettingsLoader.load(settingsFile, logger);
                logger.setDebug(settings.serverDebug());
                visor.reload(settings);
                sender.sendMessage("§aVisor settings reloaded");
            }
            case "status" -> {
                long vr = visor.sessions().all().stream().filter(VisorSession::vrActive).count();
                long total = visor.sessions().all().size();
                sender.sendMessage("§bVisor §7- sessions: §f" + total + " §7(VR: §f" + vr + "§7)");
            }
            case "trackers" -> {
                if(!sender.hasPermission(PERMISSION)){
                    sender.sendMessage("§cYou don't have permission.");
                    return true;
                }
                boolean on = visor.toggleTrackerDebug();
                if(!on){
                    BukkitPlatformPlayer.removeAllDebug(sender.getServer());
                }
                sender.sendMessage("§bVisor §7- tracker debug: " + (on ? "§aON" : "§cOFF"));
            }
            default -> sender.sendMessage("§7Unknown subcommand. /visor reload | status | trackers");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String label, String[] args){
        if(args.length == 1){
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return Stream.of("reload", "status", "trackers").filter(s -> s.startsWith(prefix)).toList();
        }
        return List.of();
    }
}

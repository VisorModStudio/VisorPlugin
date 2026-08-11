package org.vmstudio.visorplugin.bukkit.command;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.bukkit.logger.BukkitLogger;
import org.vmstudio.visorplugin.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visorplugin.common.VisorServer;
import org.vmstudio.visorplugin.common.session.VisorSession;
import org.vmstudio.visorplugin.common.settings.SettingsLoader;
import org.vmstudio.visorplugin.common.settings.VisorSettings;

public final class VisorCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "visor.admin";
    private static final List<String> SUBCOMMANDS = List.of("reload", "status", "trackers", "hands");
    private static final String USAGE = "§bVisor §7- /visor reload | status | trackers | hands";

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
            sender.sendMessage(USAGE);
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
                    BukkitPlatformPlayer.removeTrackerDebug(sender.getServer());
                }
                sender.sendMessage("§bVisor §7- tracker debug: " + (on ? "§aON" : "§cOFF"));
            }
            case "hands" -> {
                if(!sender.hasPermission(PERMISSION)){
                    sender.sendMessage("§cYou don't have permission.");
                    return true;
                }
                boolean on = visor.toggleHandDebug();
                if(!on){
                    BukkitPlatformPlayer.removeHandDebug(sender.getServer());
                }
                sender.sendMessage("§bVisor §7- hand debug: " + (on ? "§aON" : "§cOFF"));
                if(on && !visor.settings().handTrackersSupported()){
                    sender.sendMessage("§eheads up: §7handTrackersSupported is off,"
                            + " clients won't send any hand data");
                }
            }
            default -> sender.sendMessage("§7Unknown subcommand. " + USAGE);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String label, String[] args){
        if(args.length == 1){
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return SUBCOMMANDS.stream().filter(s -> s.startsWith(prefix)).toList();
        }
        return List.of();
    }
}

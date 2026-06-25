package org.vmstudio.visor.bukkit;

import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.vmstudio.visor.bukkit.adapter.VersionAdapterRegistry;
import org.vmstudio.visor.bukkit.command.VisorCommand;
import org.vmstudio.visor.bukkit.listener.PvpListener;
import org.vmstudio.visor.bukkit.listener.VisorChannelListener;
import org.vmstudio.visor.bukkit.listener.VisorLifecycleListener;
import org.vmstudio.visor.bukkit.logger.BukkitLogger;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformServer;
import org.vmstudio.visor.common.VisorServer;
import org.vmstudio.visor.common.settings.SettingsLoader;
import org.vmstudio.visor.common.settings.VisorSettings;
import org.vmstudio.visor.nms.McVersion;
import org.vmstudio.visor.nms.VersionAdapter;
import org.vmstudio.visor.protocol.VisorProtocol;

public final class VisorBukkitPlugin extends JavaPlugin {
    private VisorServer visor;
    private Path prefsFile;

    @Override
    public void onEnable(){
        BukkitLogger logger = new BukkitLogger(getLogger());

        ServerCore core = ServerCore.detect();
        Path settingsFile = getDataFolder().toPath().resolve("server_settings.yml");
        this.prefsFile = getDataFolder().toPath().resolve("vr_prefs.properties");
        VisorSettings settings = SettingsLoader.load(settingsFile, logger);
        logger.setDebug(settings.serverDebug());

        McVersion mc = McVersion.parse(getServer().getBukkitVersion());
        VersionAdapter adapter = VersionAdapterRegistry.resolve(mc, logger);
        adapter.init(this);

        BukkitPlatformServer platform = new BukkitPlatformServer(this, adapter, mc, core, logger);
        this.visor = new VisorServer(platform, settings);
        visor.prefs().loadAll(PrefsFile.load(prefsFile));

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, VisorProtocol.CHANNEL);
        messenger.registerIncomingPluginChannel(this, VisorProtocol.CHANNEL,
                new VisorChannelListener(platform, visor));
        getServer().getPluginManager().registerEvents(new VisorLifecycleListener(platform, visor), this);
        getServer().getPluginManager().registerEvents(new PvpListener(visor), this);
        getCommand("visor").setExecutor(new VisorCommand(visor, settingsFile, logger));

        visor.start();

        logger.info("VisorPlugin enabled - MC {}, {} core, adapter '{}', protocol v{}.",
                mc.raw(), core, adapter.name(), VisorProtocol.CORE_NETWORK_VERSION);
    }

    @Override
    public void onDisable(){
        if(visor != null){
            if(prefsFile != null){
                getLogger().info("Persisting " + visor.prefs().snapshot().size() + " VR prefs to " + prefsFile);
                PrefsFile.save(prefsFile, visor.prefs().snapshot());
            }
            visor.stop();
            visor = null;
        }
    }
}

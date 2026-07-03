package org.vmstudio.visorplugin.bukkit;

import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.vmstudio.visorplugin.api.VisorAPI;
import org.vmstudio.visorplugin.bukkit.adapter.VersionAdapterRegistry;
import org.vmstudio.visorplugin.bukkit.api.VisorAPIImpl;
import org.vmstudio.visorplugin.bukkit.command.VisorCommand;
import org.vmstudio.visorplugin.bukkit.listener.PvpListener;
import org.vmstudio.visorplugin.bukkit.listener.VisorChannelListener;
import org.vmstudio.visorplugin.bukkit.listener.VisorLifecycleListener;
import org.vmstudio.visorplugin.bukkit.logger.BukkitLogger;
import org.vmstudio.visorplugin.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visorplugin.bukkit.platform.BukkitPlatformServer;
import org.vmstudio.visorplugin.common.VisorServer;
import org.vmstudio.visorplugin.common.settings.SettingsLoader;
import org.vmstudio.visorplugin.common.settings.VisorSettings;
import org.vmstudio.visorplugin.api.nms.McVersion;
import org.vmstudio.visorplugin.bukkit.adapter.VisorVersionAdapter;
import org.vmstudio.visorplugin.api.network.VisorNetwork;

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
        VisorVersionAdapter adapter = VersionAdapterRegistry.resolve(mc, logger);
        adapter.init(this);

        BukkitPlatformServer platform = new BukkitPlatformServer(this, adapter, mc, core, logger);
        this.visor = new VisorServer(platform, settings);
        visor.prefs().loadAll(PrefsFile.load(prefsFile));

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, VisorNetwork.CHANNEL);
        messenger.registerIncomingPluginChannel(this, VisorNetwork.CHANNEL,
                new VisorChannelListener(platform, visor));
        getServer().getPluginManager().registerEvents(new VisorLifecycleListener(platform, visor), this);
        getServer().getPluginManager().registerEvents(new PvpListener(visor), this);
        VisorCommand visorCommand = new VisorCommand(visor, settingsFile, logger);
        getCommand("visor").setExecutor(visorCommand);
        getCommand("visor").setTabCompleter(visorCommand);

        VisorAPIImpl api = new VisorAPIImpl(visor, mc);
        visor.sessions().setListener(api);
        VisorAPI.Instance.set(api);

        visor.start();

        logger.info("VisorPlugin enabled - MC {}, {} core, adapter '{}', protocol v{}.",
                mc.raw(), core, adapter.name(), VisorNetwork.NETWORK_VERSION);
    }

    @Override
    public void onDisable(){
        VisorAPI.Instance.set(null);
        if(visor != null){
            BukkitPlatformPlayer.removeAllDebug(getServer());
            if(prefsFile != null){
                getLogger().info("Persisting " + visor.prefs().snapshot().size() + " VR prefs to " + prefsFile);
                PrefsFile.save(prefsFile, visor.prefs().snapshot());
            }
            visor.stop();
            visor = null;
        }
    }
}

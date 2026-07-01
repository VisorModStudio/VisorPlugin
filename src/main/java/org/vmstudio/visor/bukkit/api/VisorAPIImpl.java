package org.vmstudio.visor.bukkit.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.vmstudio.visor.api.VisorAPI;
import org.vmstudio.visor.api.network.VisorChannel;
import org.vmstudio.visor.api.network.VisorChannelHandler;
import org.vmstudio.visor.api.player.VRPlayer;
import org.vmstudio.visor.api.player.VisorPlayer;
import org.vmstudio.visor.api.event.VisorPlayerJoinedEvent;
import org.vmstudio.visor.api.event.VisorPlayerLeftEvent;
import org.vmstudio.visor.common.VisorServer;
import org.vmstudio.visor.common.session.SessionManager;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.api.nms.McVersion;

public final class VisorAPIImpl implements VisorAPI, SessionManager.Listener {
    private final VisorServer visor;
    private final McVersion mcVersion;

    public VisorAPIImpl(VisorServer visor, McVersion mcVersion){
        this.visor = visor;
        this.mcVersion = mcVersion;
    }

    @Override
    public McVersion minecraftVersion(){
        return mcVersion;
    }

    @Override
    public boolean isVRPlayer(UUID uuid){
        VisorSession session = visor.sessions().get(uuid);
        return session != null && session.vrActive();
    }

    @Override
    public boolean isVRPlayer(Player player) {
        return isVRPlayer(player.getUniqueId());
    }

    @Override
    public VisorPlayer getVisorPlayer(UUID uuid){
        return VisorPlayerImpl.fromVisorSession(visor.sessions().get(uuid));
    }

    @Override
    public VisorPlayer getVisorPlayer(Player player){
        return getVisorPlayer(player.getUniqueId());
    }

    @Override
    public Collection<VisorPlayer> getVisorPlayers(){
        List<VisorPlayer> out = new ArrayList<>();
        for(VisorSession session : visor.sessions().all()){
            VisorPlayer visorPlayer = VisorPlayerImpl.fromVisorSession(session);
            if(visorPlayer != null){
                out.add(visorPlayer);
            }
        }
        return out;
    }

    @Override
    public VRPlayer getVRPlayer(UUID uuid){
        return VRPlayerImpl.fromVisorSession(visor.sessions().get(uuid));
    }

    @Override
    public VRPlayer getVRPlayer(Player player){
        return getVRPlayer(player.getUniqueId());
    }

    @Override
    public Collection<VRPlayer> getVRPlayers(){
        List<VRPlayer> out = new ArrayList<>();
        for(VisorSession session : visor.sessions().all()){
            VRPlayer vrPlayer = VRPlayerImpl.fromVisorSession(session);
            if(vrPlayer != null){
                out.add(vrPlayer);
            }
        }
        return out;
    }

    @Override
    public VisorChannel registerChannel(Plugin owner, String channel){
        return registerChannel(owner, channel, null);
    }

    @Override
    public VisorChannel registerChannel(Plugin owner, String channel, VisorChannelHandler handler){
        Messenger messenger = Bukkit.getMessenger();
        VisorChannelImpl impl = new VisorChannelImpl(owner, channel, handler);
        PluginMessageListener listener = (ch, player, message) -> {
            if(ch.equals(channel)){
                impl.handleIncoming(player, message);
            }
        };
        impl.attachListener(listener);
        messenger.registerOutgoingPluginChannel(owner, channel);
        messenger.registerIncomingPluginChannel(owner, channel, listener);
        return impl;
    }

    @Override
    public void onSessionStart(VisorSession session){
        VisorPlayer visorPlayer = VisorPlayerImpl.fromVisorSession(session);
        if(visorPlayer != null){
            Bukkit.getPluginManager().callEvent(new VisorPlayerJoinedEvent(visorPlayer));
        }
    }

    @Override
    public void onSessionEnd(VisorSession session){
        VisorPlayer visorPlayer = VisorPlayerImpl.fromVisorSession(session);
        if(visorPlayer != null){
            Bukkit.getPluginManager().callEvent(new VisorPlayerLeftEvent(visorPlayer));
        }
    }
}

package org.vmstudio.visor.bukkit.api;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.player.VisorPlayer;
import org.vmstudio.visor.bukkit.platform.BukkitPlatformPlayer;
import org.vmstudio.visor.common.session.VisorSession;

public class VisorPlayerImpl implements VisorPlayer {
    public final VisorSession session;
    public final Player player;

    public VisorPlayerImpl(VisorSession session, Player player){
        this.session = session;
        this.player = player;
    }

    @Override
    public UUID getUuid(){
        return player.getUniqueId();
    }

    @Override
    public Player getMcPlayer(){
        return player;
    }

    @Override
    public String getClientVersion(){
        return session.clientVersion();
    }

    public static @Nullable VisorPlayer fromVisorSession(VisorSession session){
        if(session == null){
            return null;
        }
        Player player = ((BukkitPlatformPlayer) session.player()).handle();
        return session.vrActive() ? new VRPlayerImpl(session, player) : new VisorPlayerImpl(session, player);
    }
}

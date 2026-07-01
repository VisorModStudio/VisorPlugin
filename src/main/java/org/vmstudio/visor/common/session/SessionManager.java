package org.vmstudio.visor.common.session;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Setter;
import org.vmstudio.visor.common.platform.PlatformPlayer;

public final class SessionManager {
    private final ConcurrentHashMap<UUID, VisorSession> sessions = new ConcurrentHashMap<>();
    @Setter
    private Listener listener;

    public VisorSession register(PlatformPlayer player, boolean vrActive, String clientVersion){
        VisorSession session = new VisorSession(player, vrActive, clientVersion);
        sessions.put(player.uuid(), session);
        if(listener != null){
            listener.onSessionStart(session);
        }
        return session;
    }

    public VisorSession get(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean has(UUID uuid){
        return sessions.containsKey(uuid);
    }

    public VisorSession remove(UUID uuid){
        VisorSession session = sessions.remove(uuid);
        if(listener != null && session != null){
            listener.onSessionEnd(session);
        }
        return session;
    }

    public Collection<VisorSession> all(){
        return sessions.values();
    }
    public interface Listener {
        void onSessionStart(VisorSession session);
        void onSessionEnd(VisorSession session);
    }
}

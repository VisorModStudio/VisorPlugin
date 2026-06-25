package org.vmstudio.visor.common.session;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.vmstudio.visor.common.platform.PlatformPlayer;

public final class SessionManager {
    private final ConcurrentHashMap<UUID, VisorSession> sessions = new ConcurrentHashMap<>();

    public VisorSession register(PlatformPlayer player, boolean vrActive, String clientVersion){
        VisorSession session = new VisorSession(player, vrActive, clientVersion);
        sessions.put(player.uuid(), session);
        return session;
    }

    public VisorSession get(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean has(UUID uuid){
        return sessions.containsKey(uuid);
    }

    public VisorSession remove(UUID uuid){
        return sessions.remove(uuid);
    }

    public Collection<VisorSession> all(){
        return sessions.values();
    }
}

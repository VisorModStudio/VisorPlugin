package org.vmstudio.visor.common;

import java.util.UUID;

import org.vmstudio.visor.common.broadcast.TrackerBroadcaster;
import org.vmstudio.visor.common.handler.InboundHandler;
import org.vmstudio.visor.common.platform.Cancellable;
import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.platform.PlatformServer;
import org.vmstudio.visor.common.session.SessionManager;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.common.session.VrPrefsStore;
import org.vmstudio.visor.common.session.VrState;
import org.vmstudio.visor.common.settings.VisorSettings;
import org.vmstudio.visor.protocol.VisorCodec;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.toclient.ServerSettingsOut;
import org.vmstudio.visor.protocol.value.Vec3f;

public final class VisorServer {
    private final PlatformServer platform;
    private final SessionManager sessions = new SessionManager();
    private final VrPrefsStore prefs = new VrPrefsStore();
    private final InboundHandler handler;
    private final TrackerBroadcaster broadcaster;

    private volatile VisorSettings settings;
    private volatile boolean trackerDebug;
    private Cancellable tickTask;

    public VisorServer(PlatformServer platform, VisorSettings settings){
        this.platform = platform;
        this.settings = settings;
        this.handler = new InboundHandler(sessions, prefs, () -> this.settings, platform.logger());
        this.broadcaster = new TrackerBroadcaster(sessions);
    }

    public void start(){
        tickTask = platform.scheduler().runGlobalTimer(this::tickAll, 1L);
        platform.logger().info("VisorServer started (MC {}, Paper={}, Folia={})",
                platform.minecraftVersion(), platform.isPaper(), platform.isFolia());
    }

    public void stop(){
        if(tickTask != null){
            tickTask.cancel();
            tickTask = null;
        }
    }

    public void onMessage(PlatformPlayer player, byte[] data){
        try {
            VisorInbound inbound = VisorCodec.decodeInbound(data);
            if(inbound == null){
                if(platform.logger().debugEnabled()){
                    platform.logger().warn("Ignoring unrecognised Visor packet from {}", player.name());
                }
                return;
            }
            handler.handle(player, inbound);
        }catch (RuntimeException e){
            platform.logger().warn("Error handling Visor packet from {}: {}", player.name(), e.toString());
        }
    }

    public void onJoin(PlatformPlayer player){
        if(!settings.vrOnly() || player.isOp()){
            return;
        }
        platform.scheduler().runForPlayerDelayed(player, () -> {
            if(!player.isOnline() || player.isOp()){
                return;
            }
            VisorSession session = sessions.get(player.uuid());
            if(session == null || !session.vrActive()){
                player.disconnect("This server is VR-only.");
            }
        }, 200L);
    }

    public void onQuit(UUID uuid){
        VisorSession session = sessions.get(uuid);
        if(session != null && session.vrActive()){
            session.player().clearTrackerDebug();
        }
        sessions.remove(uuid);
    }

    public boolean toggleTrackerDebug(){
        trackerDebug = !trackerDebug;
        return trackerDebug;
    }

    public boolean trackerDebug(){
        return trackerDebug;
    }

    public VisorSettings settings(){
        return settings;
    }

    public void reload(VisorSettings settings){
        this.settings = settings;
        broadcastSettings();
    }

    public void broadcastSettings(){
        byte[] bytes = VisorCodec.encode(new ServerSettingsOut(settings.toClientYaml()));
        for(VisorSession session : sessions.all()){
            session.player().sendPayload(bytes);
        }
    }

    public SessionManager sessions(){
        return sessions;
    }

    public VrPrefsStore prefs(){
        return prefs;
    }

    private void tickAll(){
        VisorSettings s = settings;
        for(VisorSession session : sessions.all()){
            if(!session.vrActive()){
                continue;
            }
            platform.scheduler().runForPlayer(session.player(), () -> tickPlayer(session, s));
        }
    }

    private void tickPlayer(VisorSession session, VisorSettings s){
        broadcaster.broadcast(session);
        session.player().tickEffects();

        VrState vr = session.vr();
        if(!trackerDebug && s.creeperSwellDistance() > 0 && vr.pose() != null){
            Vec3f hmd = vr.pose().hmd().position();
            session.player().swellNearbyCreepers(s.creeperSwellDistance(), hmd.x(), hmd.y(), hmd.z());
        }
        if(trackerDebug){
            session.player().showTrackerDebug(vr.pose());
        }
    }
}

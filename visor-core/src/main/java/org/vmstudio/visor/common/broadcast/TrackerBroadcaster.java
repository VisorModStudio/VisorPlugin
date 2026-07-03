package org.vmstudio.visor.common.broadcast;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.session.SessionManager;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.common.session.VrState;
import org.vmstudio.visor.protocol.VisorCodec;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherBodyTypePayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherFullHeightPayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherGunAnglePayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherLeftHandedPayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherOverlayFocusedPayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherPoseDataPayloadToClient;
import org.vmstudio.visor.protocol.toclient.OtherWorldScalePayloadToClient;

public final class TrackerBroadcaster {
    private final SessionManager sessions;

    public TrackerBroadcaster(SessionManager sessions){
        this.sessions = sessions;
    }

    public void broadcast(VisorSession session){
        if(!session.vrActive()){
            return;
        }
        VrState vr = session.vr();
        if(vr.pose() == null){
            return;
        }

        PlatformPlayer self = session.player();
        UUID uuid = self.uuid();

        Set<PlatformPlayer> trackers = new HashSet<>();
        for(PlatformPlayer t : self.trackers()){
            if(!t.uuid().equals(uuid) && sessions.has(t.uuid())){
                trackers.add(t);
            }
        }
        Set<UUID> currentUuids = new HashSet<>();
        for(PlatformPlayer t : trackers){
            currentUuids.add(t.uuid());
        }
        Set<UUID> newUuids = new HashSet<>(currentUuids);
        newUuids.removeAll(vr.getKnownTrackers());

        byte[] pose = VisorCodec.encode(new OtherPoseDataPayloadToClient(uuid, vr.pose()));
        for(PlatformPlayer t : trackers){
            t.sendPayload(pose);
        }

        if(!newUuids.isEmpty()){
            byte[] body = VisorCodec.encode(new OtherBodyTypePayloadToClient(uuid, vr.getBodyType()));
            byte[] left = VisorCodec.encode(new OtherLeftHandedPayloadToClient(uuid, vr.isLeftHanded()));
            byte[] scale = VisorCodec.encode(new OtherWorldScalePayloadToClient(uuid, vr.getWorldScale()));
            byte[] height = VisorCodec.encode(new OtherFullHeightPayloadToClient(uuid, vr.getFullHeight()));
            byte[] gun = VisorCodec.encode(new OtherGunAnglePayloadToClient(uuid, vr.getGunAngle()));
            byte[] overlay = VisorCodec.encode(new OtherOverlayFocusedPayloadToClient(uuid, vr.isOverlayFocused()));
            for(PlatformPlayer t : trackers){
                if(!newUuids.contains(t.uuid())){
                    continue;
                }
                t.sendPayload(body);
                t.sendPayload(left);
                t.sendPayload(scale);
                t.sendPayload(height);
                t.sendPayload(gun);
                t.sendPayload(overlay);
            }
        }

        if(!vr.getBodyType().equals(vr.getBodyTypeLastSent())){
            sendDelta(trackers, newUuids, new OtherBodyTypePayloadToClient(uuid, vr.getBodyType()));
            vr.setBodyTypeLastSent(vr.getBodyType());
        }
        if(vr.isLeftHanded() != vr.isLeftHandedLastSent()){
            sendDelta(trackers, newUuids, new OtherLeftHandedPayloadToClient(uuid, vr.isLeftHanded()));
            vr.setLeftHandedLastSent(vr.isLeftHanded());
        }
        if(vr.getWorldScale() != vr.getWorldScaleLastSent()){
            sendDelta(trackers, newUuids, new OtherWorldScalePayloadToClient(uuid, vr.getWorldScale()));
            vr.setWorldScaleLastSent(vr.getWorldScale());
        }
        if(vr.getFullHeight() != vr.getFullHeightLastSent()){
            sendDelta(trackers, newUuids, new OtherFullHeightPayloadToClient(uuid, vr.getFullHeight()));
            vr.setFullHeightLastSent(vr.getFullHeight());
        }
        if(vr.getGunAngle() != vr.getGunAngleLastSent()){
            sendDelta(trackers, null, new OtherGunAnglePayloadToClient(uuid, vr.getGunAngle()));
            vr.setGunAngleLastSent(vr.getGunAngle());
        }
        if(vr.isOverlayFocused() != vr.isOverlayFocusedLastSent()){
            sendDelta(trackers, newUuids, new OtherOverlayFocusedPayloadToClient(uuid, vr.isOverlayFocused()));
            vr.setOverlayFocusedLastSent(vr.isOverlayFocused());
        }

        vr.setKnownTrackers(currentUuids);
    }

    private static void sendDelta(Set<PlatformPlayer> trackers, Set<UUID> exclude, VisorCorePayloadToClient payload){
        byte[] bytes = VisorCodec.encode(payload);
        for(PlatformPlayer t : trackers){
            if(exclude != null && exclude.contains(t.uuid())){
                continue;
            }
            t.sendPayload(bytes);
        }
    }
}

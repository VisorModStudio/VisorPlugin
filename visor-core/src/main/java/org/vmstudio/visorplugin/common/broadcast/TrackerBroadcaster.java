package org.vmstudio.visorplugin.common.broadcast;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.vmstudio.visorplugin.common.platform.PlatformPlayer;
import org.vmstudio.visorplugin.common.session.SessionManager;
import org.vmstudio.visorplugin.common.session.VisorSession;
import org.vmstudio.visorplugin.common.session.VrState;
import org.vmstudio.visorplugin.protocol.VisorCodec;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherBodyTypePayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherFullHeightPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherGunAnglePayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherLeftHandedPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherOverlayFocusedPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherPoseDataPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherRotationYPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherStartTrackingPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.other.OtherWorldScalePayloadToClient;

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

        // ----- Send the initial VR state bundle to new trackers -----
        if(!newUuids.isEmpty()){
            byte[] start = VisorCodec.encode(new OtherStartTrackingPayloadToClient(
                    uuid,
                    new OtherPoseDataPayloadToClient(uuid, vr.pose()),
                    new OtherBodyTypePayloadToClient(uuid, vr.getBodyType()),
                    new OtherLeftHandedPayloadToClient(uuid, vr.isLeftHanded()),
                    new OtherRotationYPayloadToClient(uuid, vr.getRotationY()),
                    new OtherWorldScalePayloadToClient(uuid, vr.getWorldScale()),
                    new OtherFullHeightPayloadToClient(uuid, vr.getFullHeight()),
                    new OtherGunAnglePayloadToClient(uuid, vr.getGunAngle()),
                    new OtherOverlayFocusedPayloadToClient(uuid, vr.isOverlayFocused())
            ));
            for(PlatformPlayer t : trackers){
                if(newUuids.contains(t.uuid())){
                    t.sendPayload(start);
                }
            }
        }

        // ----- Send changed state to existing trackers (new trackers already got it above) -----
        if(!vr.getBodyType().equals(vr.getBodyTypeLastSent())){
            sendDelta(trackers, newUuids, new OtherBodyTypePayloadToClient(uuid, vr.getBodyType()));
            vr.setBodyTypeLastSent(vr.getBodyType());
        }
        if(vr.isLeftHanded() != vr.isLeftHandedLastSent()){
            sendDelta(trackers, newUuids, new OtherLeftHandedPayloadToClient(uuid, vr.isLeftHanded()));
            vr.setLeftHandedLastSent(vr.isLeftHanded());
        }
        if(vr.getRotationY() != vr.getRotationYLastSent()){
            sendDelta(trackers, newUuids, new OtherRotationYPayloadToClient(uuid, vr.getRotationY()));
            vr.setRotationYLastSent(vr.getRotationY());
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

        byte[] pose = VisorCodec.encode(new OtherPoseDataPayloadToClient(uuid, vr.pose()));
        for(PlatformPlayer t : trackers){
            if(!newUuids.contains(t.uuid())){
                t.sendPayload(pose);
            }
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

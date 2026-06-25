package org.vmstudio.visor.common.broadcast;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.session.SessionManager;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.common.session.VrState;
import org.vmstudio.visor.protocol.VisorCodec;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.toclient.OtherBodyTypeOut;
import org.vmstudio.visor.protocol.toclient.OtherFullHeightOut;
import org.vmstudio.visor.protocol.toclient.OtherGunAngleOut;
import org.vmstudio.visor.protocol.toclient.OtherLeftHandedOut;
import org.vmstudio.visor.protocol.toclient.OtherOverlayFocusedOut;
import org.vmstudio.visor.protocol.toclient.OtherPoseDataOut;
import org.vmstudio.visor.protocol.toclient.OtherWorldScaleOut;

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
        newUuids.removeAll(vr.knownTrackers());

        byte[] pose = VisorCodec.encode(new OtherPoseDataOut(uuid, vr.pose()));
        for(PlatformPlayer t : trackers){
            t.sendPayload(pose);
        }

        if(!newUuids.isEmpty()){
            byte[] body = VisorCodec.encode(new OtherBodyTypeOut(uuid, vr.bodyType()));
            byte[] left = VisorCodec.encode(new OtherLeftHandedOut(uuid, vr.leftHanded()));
            byte[] scale = VisorCodec.encode(new OtherWorldScaleOut(uuid, vr.worldScale()));
            byte[] height = VisorCodec.encode(new OtherFullHeightOut(uuid, vr.fullHeight()));
            byte[] gun = VisorCodec.encode(new OtherGunAngleOut(uuid, vr.gunAngle()));
            byte[] overlay = VisorCodec.encode(new OtherOverlayFocusedOut(uuid, vr.overlayFocused()));
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

        if(!vr.bodyType().equals(vr.bodyTypeLastSent())){
            sendDelta(trackers, newUuids, new OtherBodyTypeOut(uuid, vr.bodyType()));
            vr.setBodyTypeLastSent(vr.bodyType());
        }
        if(vr.leftHanded() != vr.leftHandedLastSent()){
            sendDelta(trackers, newUuids, new OtherLeftHandedOut(uuid, vr.leftHanded()));
            vr.setLeftHandedLastSent(vr.leftHanded());
        }
        if(vr.worldScale() != vr.worldScaleLastSent()){
            sendDelta(trackers, newUuids, new OtherWorldScaleOut(uuid, vr.worldScale()));
            vr.setWorldScaleLastSent(vr.worldScale());
        }
        if(vr.fullHeight() != vr.fullHeightLastSent()){
            sendDelta(trackers, newUuids, new OtherFullHeightOut(uuid, vr.fullHeight()));
            vr.setFullHeightLastSent(vr.fullHeight());
        }
        if(vr.gunAngle() != vr.gunAngleLastSent()){
            sendDelta(trackers, null, new OtherGunAngleOut(uuid, vr.gunAngle()));
            vr.setGunAngleLastSent(vr.gunAngle());
        }
        if(vr.overlayFocused() != vr.overlayFocusedLastSent()){
            sendDelta(trackers, newUuids, new OtherOverlayFocusedOut(uuid, vr.overlayFocused()));
            vr.setOverlayFocusedLastSent(vr.overlayFocused());
        }

        vr.setKnownTrackers(currentUuids);
    }

    private static void sendDelta(Set<PlatformPlayer> trackers, Set<UUID> exclude, VisorOutbound payload){
        byte[] bytes = VisorCodec.encode(payload);
        for(PlatformPlayer t : trackers){
            if(exclude != null && exclude.contains(t.uuid())){
                continue;
            }
            t.sendPayload(bytes);
        }
    }
}

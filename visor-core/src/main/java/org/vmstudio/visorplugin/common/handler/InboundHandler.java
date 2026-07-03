package org.vmstudio.visorplugin.common.handler;

import org.vmstudio.visorplugin.common.platform.PlatformPlayer;
import org.vmstudio.visorplugin.common.platform.VisorLogger;
import org.vmstudio.visorplugin.common.session.*;
import org.vmstudio.visorplugin.api.player.SupportedMovement;
import org.vmstudio.visorplugin.common.session.*;
import org.vmstudio.visorplugin.common.settings.VisorSettings;
import org.vmstudio.visorplugin.protocol.VisorCodec;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.api.network.VisorNetwork;
import org.vmstudio.visorplugin.protocol.toclient.HandshakePayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.OffhandSlotPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.RotationYPayloadToClient;
import org.vmstudio.visorplugin.protocol.toclient.ServerSettingsPayloadToClient;
import org.vmstudio.visorplugin.protocol.toserver.*;
import org.vmstudio.visorplugin.protocol.toserver.*;

import java.util.function.Supplier;

public final class InboundHandler {
    private final SessionManager sessions;
    private final VrPrefsStore prefs;
    private final Supplier<VisorSettings> settings;
    private final VisorLogger logger;

    public InboundHandler(SessionManager sessions, VrPrefsStore prefs,
                          Supplier<VisorSettings> settings, VisorLogger logger) {
        this.sessions = sessions;
        this.prefs = prefs;
        this.settings = settings;
        this.logger = logger;
    }

    public void handle(PlatformPlayer sender, VisorCorePayloadToServer payload){
        VisorSession session = sessions.get(sender.uuid());

        if (session == null) {
            if (payload instanceof HandshakePayloadToServer hs) {
                handleHandshake(sender, hs);
            }
            return;
        }
        if (!session.vrActive()) {
            return;
        }
        if (payload instanceof HandshakePayloadToServer) {
            return;
        }

        VrState vr = session.vr();
        VisorSettings s = settings.get();

        if (payload instanceof PoseDataPayloadToServer p) {
            vr.setPose(p.pose());
            vr.pushPoseSnapshot(p.pose(), sender.x(), sender.y(), sender.z());
        } else if (payload instanceof LeftHandedPayloadToServer p) {
            vr.setLeftHanded(p.leftHanded());
        } else if (payload instanceof ActiveHandPayloadToServer p) {
            vr.setActiveHandMain(p.activeHandMain());
        } else if (payload instanceof OffhandSlotPayloadToServer p) {
            vr.setOffhandSlot(p.slot());
            prefs.setOffhandSlot(sender.uuid(), p.slot());
        } else if (payload instanceof VrBodyTypePayloadToServer p) {
            vr.setBodyType(p.bodyType());
        } else if (payload instanceof WorldScalePayloadToServer p) {
            vr.setWorldScale(p.worldScale());
        } else if (payload instanceof FullHeightPayloadToServer p) {
            vr.setFullHeight(p.fullHeight());
        } else if (payload instanceof RotationYPayloadToServer p) {
            vr.setRotationY(p.rotationY());
            prefs.setRotationY(sender.uuid(), p.rotationY());
        } else if (payload instanceof GunAnglePayloadToServer p) {
            vr.setGunAngle(p.gunAngle());
        } else if (payload instanceof OverlayFocusedPayloadToServer p) {
            vr.setOverlayFocused(p.overlayFocused());
        } else if (payload instanceof CrawlingPayloadToServer p) {
            if (s.roomCrawlingSupported()) {
                vr.setCrawling(p.crawling());
                sender.setForcedCrawl(p.crawling());
            }
        } else if (payload instanceof ClimbingPayloadToServer) {
            if (s.roomClimbingSupported()) {
                sender.resetFallDistance();
            }
        } else if (payload instanceof TeleportPayloadToServer p) {
            if (s.supportedMovement() != SupportedMovement.CONTROLLER) {
                sender.teleportAbsolute(p.x(), p.y(), p.z());
            }
        } else if (payload instanceof SwingAttackPayloadToServer p) {
            if (s.betterSwinging()) {
                sender.swingAttack(p.entityId(), p.shiftKeyDown(), p.mainHand());
            }
        } else if (payload instanceof SwingBlockPayloadToServer p) {
            if (s.betterSwinging()) {
                sender.blockSwing(p.blockPos(), p.direction(), p.mainHand(), p.sequence(), s.swingingRepairDelay());
            }
        }
    }

    private void handleHandshake(PlatformPlayer player, HandshakePayloadToServer hs) {
        VisorSettings s = settings.get();

        if (hs.networkVersion() != VisorNetwork.NETWORK_VERSION) {
            player.disconnect("Visor network version mismatch: client " + hs.networkVersion()
                    + ", server " + VisorNetwork.NETWORK_VERSION);
            return;
        }

        sessions.register(player, hs.vrActive(), hs.visorVersion());
        if (s.serverDebug()) {
            logger.info("Visor: {} joined ({}), VR={}", player.name(), hs.visorVersion(), hs.vrActive());
        }

        player.sendPayload(VisorCodec.encode(new HandshakePayloadToClient()));
        player.sendPayload(VisorCodec.encode(new ServerSettingsPayloadToClient(s.toClientYaml())));

        if (hs.vrActive()) {
            VrPrefs pr = prefs.get(player.uuid());
            player.sendPayload(VisorCodec.encode(new RotationYPayloadToClient(pr.rotationY())));
            if (s.twoHandedVR()) {
                player.sendPayload(VisorCodec.encode(new OffhandSlotPayloadToClient(pr.offhandSlot())));
            }
        }
    }
}

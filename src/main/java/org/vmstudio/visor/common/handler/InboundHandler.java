package org.vmstudio.visor.common.handler;

import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.common.platform.VisorLogger;
import org.vmstudio.visor.common.session.*;
import org.vmstudio.visor.common.settings.SupportedMovement;
import org.vmstudio.visor.common.settings.VisorSettings;
import org.vmstudio.visor.protocol.VisorCodec;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorProtocol;
import org.vmstudio.visor.protocol.toclient.HandshakeOut;
import org.vmstudio.visor.protocol.toclient.OffhandSlotOut;
import org.vmstudio.visor.protocol.toclient.RotationYOut;
import org.vmstudio.visor.protocol.toclient.ServerSettingsOut;
import org.vmstudio.visor.protocol.toserver.*;

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

    public void handle(PlatformPlayer sender, VisorInbound payload){
        VisorSession session = sessions.get(sender.uuid());

        if (session == null) {
            if (payload instanceof HandshakeIn hs) {
                handleHandshake(sender, hs);
            }
            return;
        }
        if (!session.vrActive()) {
            return;
        }
        if (payload instanceof HandshakeIn) {
            return;
        }

        VrState vr = session.vr();
        VisorSettings s = settings.get();

        if (payload instanceof PoseDataIn p) {
            vr.setPose(p.pose());
        } else if (payload instanceof LeftHandedIn p) {
            vr.setLeftHanded(p.leftHanded());
        } else if (payload instanceof ActiveHandIn p) {
            vr.setActiveHandMain(p.activeHandMain());
        } else if (payload instanceof OffhandSlotIn p) {
            vr.setOffhandSlot(p.slot());
            prefs.setOffhandSlot(sender.uuid(), p.slot());
        } else if (payload instanceof VrBodyTypeIn p) {
            vr.setBodyType(p.bodyType());
        } else if (payload instanceof WorldScaleIn p) {
            vr.setWorldScale(p.worldScale());
        } else if (payload instanceof FullHeightIn p) {
            vr.setFullHeight(p.fullHeight());
        } else if (payload instanceof RotationYIn p) {
            vr.setRotationY(p.rotationY());
            prefs.setRotationY(sender.uuid(), p.rotationY());
        } else if (payload instanceof GunAngleIn p) {
            vr.setGunAngle(p.gunAngle());
        } else if (payload instanceof OverlayFocusedIn p) {
            vr.setOverlayFocused(p.overlayFocused());
        } else if (payload instanceof CrawlingIn p) {
            if (s.roomCrawlingSupported()) {
                vr.setCrawling(p.crawling());
                sender.setForcedCrawl(p.crawling());
            }
        } else if (payload instanceof ClimbingIn) {
            if (s.roomClimbingSupported()) {
                sender.resetFallDistance();
            }
        } else if (payload instanceof TeleportIn p) {
            if (s.supportedMovement() != SupportedMovement.CONTROLLER) {
                sender.teleportAbsolute(p.x(), p.y(), p.z());
            }
        } else if (payload instanceof SwingAttackIn p) {
            if (s.betterSwinging()) {
                sender.swingAttack(p.entityId(), p.shiftKeyDown(), p.mainHand());
            }
        } else if (payload instanceof SwingBlockIn p) {
            if (s.betterSwinging()) {
                sender.blockSwing(p.blockPos(), p.direction(), p.mainHand(), p.sequence(), s.swingingRepairDelay());
            }
        }
    }

    private void handleHandshake(PlatformPlayer player, HandshakeIn hs) {
        VisorSettings s = settings.get();

        if (hs.networkVersion() != VisorProtocol.CORE_NETWORK_VERSION) {
            player.disconnect("Visor network version mismatch: client " + hs.networkVersion()
                    + ", server " + VisorProtocol.CORE_NETWORK_VERSION);
            return;
        }

        sessions.register(player, hs.vrActive(), hs.visorVersion());
        if (s.serverDebug()) {
            logger.info("Visor: {} joined ({}), VR={}", player.name(), hs.visorVersion(), hs.vrActive());
        }

        player.sendPayload(VisorCodec.encode(new HandshakeOut()));
        player.sendPayload(VisorCodec.encode(new ServerSettingsOut(s.toClientYaml())));

        if (hs.vrActive()) {
            VrPrefs pr = prefs.get(player.uuid());
            player.sendPayload(VisorCodec.encode(new RotationYOut(pr.rotationY())));
            if (s.twoHandedVR()) {
                player.sendPayload(VisorCodec.encode(new OffhandSlotOut(pr.offhandSlot())));
            }
        }
    }
}

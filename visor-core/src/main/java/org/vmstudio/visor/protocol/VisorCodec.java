package org.vmstudio.visor.protocol;

import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.api.network.VisorPayload;
import org.vmstudio.visor.protocol.toserver.ActiveHandPayloadToServer;
import org.vmstudio.visor.protocol.toserver.ClimbingPayloadToServer;
import org.vmstudio.visor.protocol.toserver.CrawlingPayloadToServer;
import org.vmstudio.visor.protocol.toserver.FullHeightPayloadToServer;
import org.vmstudio.visor.protocol.toserver.GunAnglePayloadToServer;
import org.vmstudio.visor.protocol.toserver.HandshakePayloadToServer;
import org.vmstudio.visor.protocol.toserver.LeftHandedPayloadToServer;
import org.vmstudio.visor.protocol.toserver.OffhandSlotPayloadToServer;
import org.vmstudio.visor.protocol.toserver.OverlayFocusedPayloadToServer;
import org.vmstudio.visor.protocol.toserver.PoseDataPayloadToServer;
import org.vmstudio.visor.protocol.toserver.RotationYPayloadToServer;
import org.vmstudio.visor.protocol.toserver.SwingAttackPayloadToServer;
import org.vmstudio.visor.protocol.toserver.SwingBlockPayloadToServer;
import org.vmstudio.visor.protocol.toserver.TeleportPayloadToServer;
import org.vmstudio.visor.protocol.toserver.VrBodyTypePayloadToServer;
import org.vmstudio.visor.protocol.toserver.WorldScalePayloadToServer;

public final class VisorCodec {
    private VisorCodec(){}

    public static byte[] encode(VisorPayload payload){
        VisorByteBuf buf = VisorByteBuf.create();
        payload.write(buf);
        return buf.toBytes();
    }

    public static VisorCorePayloadToServer decodeInbound(byte[] data){
        if(data == null || data.length == 0){
            return null;
        }
        VisorByteBuf buf = VisorByteBuf.wrap(data);
        VisorCorePayloadId id = VisorCorePayloadId.fromOrdinal(buf.readByte());
        if(id == null){
            return null;
        }
        return switch(id){
            case HANDSHAKE -> HandshakePayloadToServer.read(buf);
            case POSE_DATA -> PoseDataPayloadToServer.read(buf);
            case LEFT_HANDED -> LeftHandedPayloadToServer.read(buf);
            case ACTIVE_HAND -> ActiveHandPayloadToServer.read(buf);
            case OFFHAND_SLOT -> OffhandSlotPayloadToServer.read(buf);
            case VR_BODY_TYPE -> VrBodyTypePayloadToServer.read(buf);
            case WORLD_SCALE -> WorldScalePayloadToServer.read(buf);
            case FULL_HEIGHT -> FullHeightPayloadToServer.read(buf);
            case ROTATION_Y -> RotationYPayloadToServer.read(buf);
            case GUN_ANGLE -> GunAnglePayloadToServer.read(buf);
            case OVERLAY_FOCUSED -> OverlayFocusedPayloadToServer.read(buf);
            case CRAWLING -> CrawlingPayloadToServer.read(buf);
            case CLIMBING -> ClimbingPayloadToServer.read(buf);
            case TELEPORT -> TeleportPayloadToServer.read(buf);
            case SWING_ATTACK -> SwingAttackPayloadToServer.read(buf);
            case SWING_BLOCK -> SwingBlockPayloadToServer.read(buf);
            default -> null;
        };
    }
}

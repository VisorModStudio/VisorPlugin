package org.vmstudio.visor.protocol;

import org.vmstudio.visor.protocol.toclient.BlockDamageOut;
import org.vmstudio.visor.protocol.toclient.HandshakeOut;
import org.vmstudio.visor.protocol.toclient.OffhandSlotOut;
import org.vmstudio.visor.protocol.toclient.OtherBodyTypeOut;
import org.vmstudio.visor.protocol.toclient.OtherFullHeightOut;
import org.vmstudio.visor.protocol.toclient.OtherGunAngleOut;
import org.vmstudio.visor.protocol.toclient.OtherLeftHandedOut;
import org.vmstudio.visor.protocol.toclient.OtherOverlayFocusedOut;
import org.vmstudio.visor.protocol.toclient.OtherPoseDataOut;
import org.vmstudio.visor.protocol.toclient.OtherWorldScaleOut;
import org.vmstudio.visor.protocol.toclient.RotationYOut;
import org.vmstudio.visor.protocol.toclient.ServerSettingsOut;
import org.vmstudio.visor.protocol.toserver.ActiveHandIn;
import org.vmstudio.visor.protocol.toserver.ClimbingIn;
import org.vmstudio.visor.protocol.toserver.CrawlingIn;
import org.vmstudio.visor.protocol.toserver.FullHeightIn;
import org.vmstudio.visor.protocol.toserver.GunAngleIn;
import org.vmstudio.visor.protocol.toserver.HandshakeIn;
import org.vmstudio.visor.protocol.toserver.LeftHandedIn;
import org.vmstudio.visor.protocol.toserver.OffhandSlotIn;
import org.vmstudio.visor.protocol.toserver.OverlayFocusedIn;
import org.vmstudio.visor.protocol.toserver.PoseDataIn;
import org.vmstudio.visor.protocol.toserver.RotationYIn;
import org.vmstudio.visor.protocol.toserver.SwingAttackIn;
import org.vmstudio.visor.protocol.toserver.SwingBlockIn;
import org.vmstudio.visor.protocol.toserver.TeleportIn;
import org.vmstudio.visor.protocol.toserver.VrBodyTypeIn;
import org.vmstudio.visor.protocol.toserver.WorldScaleIn;

public final class VisorCodec {
    private VisorCodec(){}

    public static byte[] encode(VisorPayload payload){
        VisorByteBuf buf = new VisorByteBuf();
        buf.writeByte(payload.id().byteOrdinal());
        payload.write(buf);
        return buf.toByteArray();
    }

    public static VisorInbound decodeInbound(byte[] data){
        if(data == null || data.length == 0){
            return null;
        }
        VisorByteBuf buf = new VisorByteBuf(data);
        VisorPayloadId id = VisorPayloadId.fromByte(buf.readByte());
        if(id == null){
            return null;
        }
        return switch(id){
            case HANDSHAKE -> HandshakeIn.read(buf);
            case POSE_DATA -> PoseDataIn.read(buf);
            case LEFT_HANDED -> LeftHandedIn.read(buf);
            case ACTIVE_HAND -> ActiveHandIn.read(buf);
            case OFFHAND_SLOT -> OffhandSlotIn.read(buf);
            case VR_BODY_TYPE -> VrBodyTypeIn.read(buf);
            case WORLD_SCALE -> WorldScaleIn.read(buf);
            case FULL_HEIGHT -> FullHeightIn.read(buf);
            case ROTATION_Y -> RotationYIn.read(buf);
            case GUN_ANGLE -> GunAngleIn.read(buf);
            case OVERLAY_FOCUSED -> OverlayFocusedIn.read(buf);
            case CRAWLING -> CrawlingIn.read(buf);
            case CLIMBING -> ClimbingIn.read(buf);
            case TELEPORT -> TeleportIn.read(buf);
            case SWING_ATTACK -> SwingAttackIn.read(buf);
            case SWING_BLOCK -> SwingBlockIn.read(buf);
            default -> null;
        };
    }

    public static VisorOutbound decodeOutbound(byte[] data){
        if(data == null || data.length == 0){
            return null;
        }
        VisorByteBuf buf = new VisorByteBuf(data);
        VisorPayloadId id = VisorPayloadId.fromByte(buf.readByte());
        if(id == null){
            return null;
        }
        return switch(id){
            case HANDSHAKE -> HandshakeOut.read(buf);
            case SERVER_SETTINGS -> ServerSettingsOut.read(buf);
            case ROTATION_Y -> RotationYOut.read(buf);
            case OFFHAND_SLOT -> OffhandSlotOut.read(buf);
            case BLOCK_DAMAGE -> BlockDamageOut.read(buf);
            case OTHER_VR_POSE_DATA -> OtherPoseDataOut.read(buf);
            case OTHER_VR_LEFT_HANDED -> OtherLeftHandedOut.read(buf);
            case OTHER_VR_BODY_TYPE -> OtherBodyTypeOut.read(buf);
            case OTHER_VR_WORLD_SCALE -> OtherWorldScaleOut.read(buf);
            case OTHER_GUN_ANGLE -> OtherGunAngleOut.read(buf);
            case OTHER_VR_FULL_HEIGHT -> OtherFullHeightOut.read(buf);
            case OTHER_VR_OVERLAY_FOCUSED -> OtherOverlayFocusedOut.read(buf);
            default -> null;
        };
    }
}

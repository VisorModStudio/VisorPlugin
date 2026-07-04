package org.vmstudio.visorplugin.protocol.toclient.other;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

/**
 * Bundles the initial VR state sent to a client that just started tracking another
 * VR player. All sub-payloads share the single leading {@code playerUUID}, so each is
 * written/read via its {@code writeSimple}/{@code readSimple} (UUID omitted).
 */
public record OtherStartTrackingPayloadToClient(UUID playerUUID,
                                                OtherPoseDataPayloadToClient poseBuf,
                                                OtherBodyTypePayloadToClient bodyTypeBuf,
                                                OtherLeftHandedPayloadToClient leftHandedBuf,
                                                OtherRotationYPayloadToClient rotationYBuf,
                                                OtherWorldScalePayloadToClient worldScaleBuf,
                                                OtherFullHeightPayloadToClient fullHeightBuf,
                                                OtherGunAnglePayloadToClient gunAngleBuf,
                                                OtherOverlayFocusedPayloadToClient overlayFocusedBuf) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_START_TRACKING;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        poseBuf.writeSimple(buf);
        bodyTypeBuf.writeSimple(buf);
        leftHandedBuf.writeSimple(buf);
        rotationYBuf.writeSimple(buf);
        worldScaleBuf.writeSimple(buf);
        fullHeightBuf.writeSimple(buf);
        gunAngleBuf.writeSimple(buf);
        overlayFocusedBuf.writeSimple(buf);
    }

    public static OtherStartTrackingPayloadToClient read(VisorByteBuf buf){
        UUID uuid = buf.readUUID();
        return new OtherStartTrackingPayloadToClient(
                uuid,
                OtherPoseDataPayloadToClient.readSimple(uuid, buf),
                OtherBodyTypePayloadToClient.readSimple(uuid, buf),
                OtherLeftHandedPayloadToClient.readSimple(uuid, buf),
                OtherRotationYPayloadToClient.readSimple(uuid, buf),
                OtherWorldScalePayloadToClient.readSimple(uuid, buf),
                OtherFullHeightPayloadToClient.readSimple(uuid, buf),
                OtherGunAnglePayloadToClient.readSimple(uuid, buf),
                OtherOverlayFocusedPayloadToClient.readSimple(uuid, buf)
        );
    }
}

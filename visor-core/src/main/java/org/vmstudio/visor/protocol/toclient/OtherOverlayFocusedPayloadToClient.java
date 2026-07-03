package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record OtherOverlayFocusedPayloadToClient(UUID playerUUID, boolean overlayFocused) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_OVERLAY_FOCUSED;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBoolean(overlayFocused);
    }

    public static OtherOverlayFocusedPayloadToClient read(VisorByteBuf buf){
        return new OtherOverlayFocusedPayloadToClient(buf.readUUID(), buf.readBoolean());
    }
}

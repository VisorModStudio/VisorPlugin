package org.vmstudio.visorplugin.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record OtherFullHeightPayloadToClient(UUID playerUUID, float fullHeight) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_FULL_HEIGHT;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(fullHeight);
    }

    public static OtherFullHeightPayloadToClient read(VisorByteBuf buf){
        return new OtherFullHeightPayloadToClient(buf.readUUID(), buf.readFloat());
    }
}

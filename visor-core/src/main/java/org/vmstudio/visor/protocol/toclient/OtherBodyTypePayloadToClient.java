package org.vmstudio.visor.protocol.toclient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record OtherBodyTypePayloadToClient(UUID playerUUID, String bodyType) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_BODY_TYPE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBytes(bodyType.getBytes(StandardCharsets.UTF_8));
    }

    public static OtherBodyTypePayloadToClient read(VisorByteBuf buf){
        return new OtherBodyTypePayloadToClient(buf.readUUID(), new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

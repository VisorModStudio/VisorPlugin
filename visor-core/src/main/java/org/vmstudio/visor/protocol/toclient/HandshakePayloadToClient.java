package org.vmstudio.visor.protocol.toclient;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record HandshakePayloadToClient() implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.HANDSHAKE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
    }

    public static HandshakePayloadToClient read(VisorByteBuf buf){
        return new HandshakePayloadToClient();
    }
}

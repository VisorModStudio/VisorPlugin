package org.vmstudio.visorplugin.protocol.toclient;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

package org.vmstudio.visorplugin.protocol.toserver;

import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record HandshakePayloadToServer(boolean vrActive, int networkVersion, String visorVersion) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.HANDSHAKE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBoolean(vrActive).writeInt(networkVersion).writeBytes(visorVersion.getBytes(StandardCharsets.UTF_8));
    }

    public static HandshakePayloadToServer read(VisorByteBuf buf){
        return new HandshakePayloadToServer(buf.readBoolean(), buf.readInt(), new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

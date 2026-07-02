package org.vmstudio.visor.protocol.toserver;

import java.nio.charset.StandardCharsets;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record HandshakeIn(boolean vrActive, int networkVersion, String visorVersion) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.HANDSHAKE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBoolean(vrActive).writeInt(networkVersion).writeBytes(visorVersion.getBytes(StandardCharsets.UTF_8));
    }

    public static HandshakeIn read(VisorBuf buf){
        return new HandshakeIn(buf.readBoolean(), buf.readInt(), new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

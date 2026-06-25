package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record HandshakeIn(boolean vrActive, int networkVersion, String visorVersion) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.HANDSHAKE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeBoolean(vrActive).writeInt(networkVersion).writeStringTail(visorVersion);
    }

    public static HandshakeIn read(VisorByteBuf buf){
        return new HandshakeIn(buf.readBoolean(), buf.readInt(), buf.readStringTail());
    }
}

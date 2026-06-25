package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record HandshakeOut() implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.HANDSHAKE;
    }

    @Override
    public void write(VisorByteBuf buf){
    }

    public static HandshakeOut read(VisorByteBuf buf){
        return new HandshakeOut();
    }
}

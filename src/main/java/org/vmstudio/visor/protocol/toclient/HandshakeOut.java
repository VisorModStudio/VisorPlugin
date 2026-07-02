package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record HandshakeOut() implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.HANDSHAKE;
    }

    @Override
    public void write(VisorBuf buf){
    }

    public static HandshakeOut read(VisorBuf buf){
        return new HandshakeOut();
    }
}

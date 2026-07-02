package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ClimbingIn() implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.CLIMBING;
    }

    @Override
    public void write(VisorBuf buf){
    }

    public static ClimbingIn read(VisorBuf buf){
        return new ClimbingIn();
    }
}

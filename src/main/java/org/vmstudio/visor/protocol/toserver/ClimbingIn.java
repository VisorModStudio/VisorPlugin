package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ClimbingIn() implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.CLIMBING;
    }

    @Override
    public void write(VisorByteBuf buf){
    }

    public static ClimbingIn read(VisorByteBuf buf){
        return new ClimbingIn();
    }
}

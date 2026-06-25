package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ActiveHandIn(boolean activeHandMain) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.ACTIVE_HAND;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeBoolean(activeHandMain);
    }

    public static ActiveHandIn read(VisorByteBuf buf){
        return new ActiveHandIn(buf.readBoolean());
    }
}

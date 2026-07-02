package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ActiveHandIn(boolean activeHandMain) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.ACTIVE_HAND;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBoolean(activeHandMain);
    }

    public static ActiveHandIn read(VisorBuf buf){
        return new ActiveHandIn(buf.readBoolean());
    }
}

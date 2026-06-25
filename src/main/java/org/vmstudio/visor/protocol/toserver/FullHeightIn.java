package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record FullHeightIn(float fullHeight) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.FULL_HEIGHT;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeFloat(fullHeight);
    }

    public static FullHeightIn read(VisorByteBuf buf){
        return new FullHeightIn(buf.readFloat());
    }
}

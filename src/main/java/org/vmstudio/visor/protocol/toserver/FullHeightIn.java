package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record FullHeightIn(float fullHeight) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.FULL_HEIGHT;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeFloat(fullHeight);
    }

    public static FullHeightIn read(VisorBuf buf){
        return new FullHeightIn(buf.readFloat());
    }
}

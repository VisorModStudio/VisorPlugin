package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record VrBodyTypeIn(String bodyType) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.VR_BODY_TYPE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeStringTail(bodyType);
    }

    public static VrBodyTypeIn read(VisorByteBuf buf){
        return new VrBodyTypeIn(buf.readStringTail());
    }
}

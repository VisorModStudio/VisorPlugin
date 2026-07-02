package org.vmstudio.visor.protocol.toserver;

import java.nio.charset.StandardCharsets;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record VrBodyTypeIn(String bodyType) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.VR_BODY_TYPE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBytes(bodyType.getBytes(StandardCharsets.UTF_8));
    }

    public static VrBodyTypeIn read(VisorBuf buf){
        return new VrBodyTypeIn(new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

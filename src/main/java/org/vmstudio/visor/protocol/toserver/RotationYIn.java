package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record RotationYIn(float rotationY) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.ROTATION_Y;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeFloat(rotationY);
    }

    public static RotationYIn read(VisorByteBuf buf){
        return new RotationYIn(buf.readFloat());
    }
}

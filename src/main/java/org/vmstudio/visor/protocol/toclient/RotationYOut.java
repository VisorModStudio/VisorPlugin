package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record RotationYOut(float rotationY) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.ROTATION_Y;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeFloat(rotationY);
    }

    public static RotationYOut read(VisorByteBuf buf){
        return new RotationYOut(buf.readFloat());
    }
}

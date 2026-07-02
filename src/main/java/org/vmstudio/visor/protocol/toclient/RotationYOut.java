package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record RotationYOut(float rotationY) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.ROTATION_Y;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeFloat(rotationY);
    }

    public static RotationYOut read(VisorBuf buf){
        return new RotationYOut(buf.readFloat());
    }
}

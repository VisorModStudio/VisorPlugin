package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record TeleportIn(float x, float y, float z) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.TELEPORT;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeFloat(x).writeFloat(y).writeFloat(z);
    }

    public static TeleportIn read(VisorByteBuf buf){
        return new TeleportIn(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }
}

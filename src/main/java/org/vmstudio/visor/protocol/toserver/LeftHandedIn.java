package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record LeftHandedIn(boolean leftHanded) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.LEFT_HANDED;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBoolean(leftHanded);
    }

    public static LeftHandedIn read(VisorBuf buf){
        return new LeftHandedIn(buf.readBoolean());
    }
}

package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record WorldScaleIn(float worldScale) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.WORLD_SCALE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeFloat(worldScale);
    }

    public static WorldScaleIn read(VisorBuf buf){
        return new WorldScaleIn(buf.readFloat());
    }
}

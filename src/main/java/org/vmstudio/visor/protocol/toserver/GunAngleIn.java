package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record GunAngleIn(float gunAngle) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.GUN_ANGLE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeFloat(gunAngle);
    }

    public static GunAngleIn read(VisorBuf buf){
        return new GunAngleIn(buf.readFloat());
    }
}

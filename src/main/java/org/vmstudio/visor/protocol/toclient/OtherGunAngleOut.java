package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherGunAngleOut(UUID playerUUID, float gunAngle) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_GUN_ANGLE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(gunAngle);
    }

    public static OtherGunAngleOut read(VisorByteBuf buf){
        return new OtherGunAngleOut(buf.readUUID(), buf.readFloat());
    }
}

package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherWorldScaleOut(UUID playerUUID, float worldScale) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_WORLD_SCALE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(worldScale);
    }

    public static OtherWorldScaleOut read(VisorByteBuf buf){
        return new OtherWorldScaleOut(buf.readUUID(), buf.readFloat());
    }
}

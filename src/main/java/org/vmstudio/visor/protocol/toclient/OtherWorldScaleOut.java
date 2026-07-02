package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherWorldScaleOut(UUID playerUUID, float worldScale) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_WORLD_SCALE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID).writeFloat(worldScale);
    }

    public static OtherWorldScaleOut read(VisorBuf buf){
        return new OtherWorldScaleOut(buf.readUUID(), buf.readFloat());
    }
}

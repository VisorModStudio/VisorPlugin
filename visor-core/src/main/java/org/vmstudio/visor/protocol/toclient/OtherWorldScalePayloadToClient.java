package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record OtherWorldScalePayloadToClient(UUID playerUUID, float worldScale) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_WORLD_SCALE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(worldScale);
    }

    public static OtherWorldScalePayloadToClient read(VisorByteBuf buf){
        return new OtherWorldScalePayloadToClient(buf.readUUID(), buf.readFloat());
    }
}

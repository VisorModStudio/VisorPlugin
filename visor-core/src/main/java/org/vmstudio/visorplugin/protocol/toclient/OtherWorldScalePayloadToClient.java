package org.vmstudio.visorplugin.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

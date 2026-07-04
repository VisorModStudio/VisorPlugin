package org.vmstudio.visorplugin.protocol.toclient.other;

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
        buf.writeUUID(playerUUID);
        writeSimple(buf);
    }

    public void writeSimple(VisorByteBuf buf){
        buf.writeFloat(worldScale);
    }

    public static OtherWorldScalePayloadToClient read(VisorByteBuf buf){
        UUID uuid = buf.readUUID();
        return readSimple(uuid, buf);
    }

    public static OtherWorldScalePayloadToClient readSimple(UUID uuid, VisorByteBuf buf){
        return new OtherWorldScalePayloadToClient(uuid, buf.readFloat());
    }
}

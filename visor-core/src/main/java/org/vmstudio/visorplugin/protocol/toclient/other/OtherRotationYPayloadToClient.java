package org.vmstudio.visorplugin.protocol.toclient.other;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OtherRotationYPayloadToClient(UUID playerUUID, float rotationY) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_ROTATION_Y;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        writeSimple(buf);
    }

    public void writeSimple(VisorByteBuf buf){
        buf.writeFloat(rotationY);
    }

    public static OtherRotationYPayloadToClient read(VisorByteBuf buf){
        UUID uuid = buf.readUUID();
        return readSimple(uuid, buf);
    }

    public static OtherRotationYPayloadToClient readSimple(UUID uuid, VisorByteBuf buf){
        return new OtherRotationYPayloadToClient(uuid, buf.readFloat());
    }
}

package org.vmstudio.visorplugin.protocol.toclient.other;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OtherBodyTypePayloadToClient(UUID playerUUID, String bodyType) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_BODY_TYPE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        writeSimple(buf);
    }

    public void writeSimple(VisorByteBuf buf){
        buf.writeString(bodyType);
    }

    public static OtherBodyTypePayloadToClient read(VisorByteBuf buf){
        UUID uuid = buf.readUUID();
        return readSimple(uuid, buf);
    }

    public static OtherBodyTypePayloadToClient readSimple(UUID uuid, VisorByteBuf buf){
        return new OtherBodyTypePayloadToClient(uuid, buf.readString());
    }
}

package org.vmstudio.visorplugin.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OtherLeftHandedPayloadToClient(UUID playerUUID, boolean leftHanded) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_LEFT_HANDED;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBoolean(leftHanded);
    }

    public static OtherLeftHandedPayloadToClient read(VisorByteBuf buf){
        return new OtherLeftHandedPayloadToClient(buf.readUUID(), buf.readBoolean());
    }
}

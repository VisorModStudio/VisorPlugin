package org.vmstudio.visorplugin.protocol.toclient.other;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OtherGunAnglePayloadToClient(UUID playerUUID, float gunAngle) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_GUN_ANGLE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(gunAngle);
    }

    public static OtherGunAnglePayloadToClient read(VisorByteBuf buf){
        return new OtherGunAnglePayloadToClient(buf.readUUID(), buf.readFloat());
    }
}

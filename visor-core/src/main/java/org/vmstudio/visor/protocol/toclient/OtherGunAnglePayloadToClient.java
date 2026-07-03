package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

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

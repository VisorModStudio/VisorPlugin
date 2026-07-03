package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record GunAnglePayloadToServer(float gunAngle) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.GUN_ANGLE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(gunAngle);
    }

    public static GunAnglePayloadToServer read(VisorByteBuf buf){
        return new GunAnglePayloadToServer(buf.readFloat());
    }
}

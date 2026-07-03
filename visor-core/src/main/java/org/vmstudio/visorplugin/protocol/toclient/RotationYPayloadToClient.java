package org.vmstudio.visorplugin.protocol.toclient;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record RotationYPayloadToClient(float rotationY) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.ROTATION_Y;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(rotationY);
    }

    public static RotationYPayloadToClient read(VisorByteBuf buf){
        return new RotationYPayloadToClient(buf.readFloat());
    }
}

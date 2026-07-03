package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record RotationYPayloadToServer(float rotationY) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.ROTATION_Y;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(rotationY);
    }

    public static RotationYPayloadToServer read(VisorByteBuf buf){
        return new RotationYPayloadToServer(buf.readFloat());
    }
}

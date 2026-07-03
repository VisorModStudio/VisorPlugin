package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record TeleportPayloadToServer(float x, float y, float z) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.TELEPORT;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(x).writeFloat(y).writeFloat(z);
    }

    public static TeleportPayloadToServer read(VisorByteBuf buf){
        return new TeleportPayloadToServer(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }
}

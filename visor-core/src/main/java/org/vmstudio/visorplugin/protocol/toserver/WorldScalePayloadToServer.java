package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record WorldScalePayloadToServer(float worldScale) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.WORLD_SCALE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(worldScale);
    }

    public static WorldScalePayloadToServer read(VisorByteBuf buf){
        return new WorldScalePayloadToServer(buf.readFloat());
    }
}

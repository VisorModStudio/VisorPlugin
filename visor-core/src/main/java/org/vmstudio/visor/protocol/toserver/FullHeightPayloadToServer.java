package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record FullHeightPayloadToServer(float fullHeight) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.FULL_HEIGHT;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeFloat(fullHeight);
    }

    public static FullHeightPayloadToServer read(VisorByteBuf buf){
        return new FullHeightPayloadToServer(buf.readFloat());
    }
}

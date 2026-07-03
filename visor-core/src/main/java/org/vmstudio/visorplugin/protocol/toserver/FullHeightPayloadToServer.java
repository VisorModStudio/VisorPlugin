package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

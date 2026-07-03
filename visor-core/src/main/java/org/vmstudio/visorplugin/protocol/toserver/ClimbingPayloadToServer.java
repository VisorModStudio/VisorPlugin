package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record ClimbingPayloadToServer() implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.CLIMBING;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
    }

    public static ClimbingPayloadToServer read(VisorByteBuf buf){
        return new ClimbingPayloadToServer();
    }
}

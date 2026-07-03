package org.vmstudio.visorplugin.protocol.toserver;

import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record VrBodyTypePayloadToServer(String bodyType) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.VR_BODY_TYPE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBytes(bodyType.getBytes(StandardCharsets.UTF_8));
    }

    public static VrBodyTypePayloadToServer read(VisorByteBuf buf){
        return new VrBodyTypePayloadToServer(new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

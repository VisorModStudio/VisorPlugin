package org.vmstudio.visor.protocol.toclient;

import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record ServerSettingsPayloadToClient(String config) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.SERVER_SETTINGS;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBytes(config.getBytes(StandardCharsets.UTF_8));
    }

    public static ServerSettingsPayloadToClient read(VisorByteBuf buf){
        return new ServerSettingsPayloadToClient(new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

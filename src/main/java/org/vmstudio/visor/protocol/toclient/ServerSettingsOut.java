package org.vmstudio.visor.protocol.toclient;

import java.nio.charset.StandardCharsets;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ServerSettingsOut(String config) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.SERVER_SETTINGS;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBytes(config.getBytes(StandardCharsets.UTF_8));
    }

    public static ServerSettingsOut read(VisorBuf buf){
        return new ServerSettingsOut(new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record ServerSettingsOut(String config) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.SERVER_SETTINGS;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeStringTail(config);
    }

    public static ServerSettingsOut read(VisorByteBuf buf){
        return new ServerSettingsOut(buf.readStringTail());
    }
}

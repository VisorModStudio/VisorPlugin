package org.vmstudio.visor.protocol.toclient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherBodyTypeOut(UUID playerUUID, String bodyType) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_BODY_TYPE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID).writeBytes(bodyType.getBytes(StandardCharsets.UTF_8));
    }

    public static OtherBodyTypeOut read(VisorBuf buf){
        return new OtherBodyTypeOut(buf.readUUID(), new String(buf.readRemainingBytes(), StandardCharsets.UTF_8));
    }
}

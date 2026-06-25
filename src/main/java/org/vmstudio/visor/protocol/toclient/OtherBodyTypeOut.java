package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherBodyTypeOut(UUID playerUUID, String bodyType) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_BODY_TYPE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeStringTail(bodyType);
    }

    public static OtherBodyTypeOut read(VisorByteBuf buf){
        return new OtherBodyTypeOut(buf.readUUID(), buf.readStringTail());
    }
}

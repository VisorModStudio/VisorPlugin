package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherFullHeightOut(UUID playerUUID, float fullHeight) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_FULL_HEIGHT;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeFloat(fullHeight);
    }

    public static OtherFullHeightOut read(VisorByteBuf buf){
        return new OtherFullHeightOut(buf.readUUID(), buf.readFloat());
    }
}

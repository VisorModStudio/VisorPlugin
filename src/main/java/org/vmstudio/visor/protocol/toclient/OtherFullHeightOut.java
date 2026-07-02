package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherFullHeightOut(UUID playerUUID, float fullHeight) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_FULL_HEIGHT;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID).writeFloat(fullHeight);
    }

    public static OtherFullHeightOut read(VisorBuf buf){
        return new OtherFullHeightOut(buf.readUUID(), buf.readFloat());
    }
}

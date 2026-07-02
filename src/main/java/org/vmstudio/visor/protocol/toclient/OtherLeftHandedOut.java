package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherLeftHandedOut(UUID playerUUID, boolean leftHanded) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_LEFT_HANDED;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID).writeBoolean(leftHanded);
    }

    public static OtherLeftHandedOut read(VisorBuf buf){
        return new OtherLeftHandedOut(buf.readUUID(), buf.readBoolean());
    }
}

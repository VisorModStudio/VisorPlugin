package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherLeftHandedOut(UUID playerUUID, boolean leftHanded) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_LEFT_HANDED;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBoolean(leftHanded);
    }

    public static OtherLeftHandedOut read(VisorByteBuf buf){
        return new OtherLeftHandedOut(buf.readUUID(), buf.readBoolean());
    }
}

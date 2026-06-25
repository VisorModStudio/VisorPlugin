package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.VBlockPos;

public record BlockDamageOut(UUID playerUUID, VBlockPos blockPos, int destroyStage) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.BLOCK_DAMAGE;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBlockPos(blockPos).writeInt(destroyStage);
    }

    public static BlockDamageOut read(VisorByteBuf buf){
        return new BlockDamageOut(buf.readUUID(), buf.readBlockPos(), buf.readInt());
    }
}

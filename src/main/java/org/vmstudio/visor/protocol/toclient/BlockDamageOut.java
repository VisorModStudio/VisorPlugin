package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.BlockPosCodec;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.VBlockPos;

public record BlockDamageOut(UUID playerUUID, VBlockPos blockPos, int destroyStage) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.BLOCK_DAMAGE;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID);
        BlockPosCodec.writeBlockPos(buf, blockPos).writeInt(destroyStage);
    }

    public static BlockDamageOut read(VisorBuf buf){
        return new BlockDamageOut(buf.readUUID(), BlockPosCodec.readBlockPos(buf), buf.readInt());
    }
}

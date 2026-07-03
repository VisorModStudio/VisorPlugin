package org.vmstudio.visorplugin.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.BlockPosCodec;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;
import org.vmstudio.visorplugin.api.network.buffer.VBlockPos;

public record BlockDamagePayloadToClient(UUID playerUUID, VBlockPos blockPos, int destroyStage) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.BLOCK_DAMAGE;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        BlockPosCodec.writeBlockPos(buf, blockPos).writeInt(destroyStage);
    }

    public static BlockDamagePayloadToClient read(VisorByteBuf buf){
        return new BlockDamagePayloadToClient(buf.readUUID(), BlockPosCodec.readBlockPos(buf), buf.readInt());
    }
}

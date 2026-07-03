package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.BlockPosCodec;
import org.vmstudio.visor.api.network.buffer.DirectionValue;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;
import org.vmstudio.visor.api.network.buffer.VBlockPos;

public record SwingBlockPayloadToServer(VBlockPos blockPos, DirectionValue direction, boolean mainHand, int sequence)
        implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.SWING_BLOCK;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        BlockPosCodec.writeBlockPos(buf, blockPos)
                .writeByte(direction.get3DDataValue())
                .writeBoolean(mainHand)
                .writeInt(sequence);
    }

    public static SwingBlockPayloadToServer read(VisorByteBuf buf){
        return new SwingBlockPayloadToServer(
                BlockPosCodec.readBlockPos(buf),
                DirectionValue.from3DDataValue(buf.readUnsignedByte()),
                buf.readBoolean(),
                buf.readInt());
    }
}

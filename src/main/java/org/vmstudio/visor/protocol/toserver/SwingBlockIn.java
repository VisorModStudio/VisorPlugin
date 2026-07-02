package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.BlockPosCodec;
import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.VBlockPos;

public record SwingBlockIn(VBlockPos blockPos, DirectionValue direction, boolean mainHand, int sequence)
        implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.SWING_BLOCK;
    }

    @Override
    public void write(VisorBuf buf){
        BlockPosCodec.writeBlockPos(buf, blockPos)
                .writeByte(direction.get3DDataValue())
                .writeBoolean(mainHand)
                .writeInt(sequence);
    }

    public static SwingBlockIn read(VisorBuf buf){
        return new SwingBlockIn(
                BlockPosCodec.readBlockPos(buf),
                DirectionValue.from3DDataValue(buf.readUnsignedByte()),
                buf.readBoolean(),
                buf.readInt());
    }
}

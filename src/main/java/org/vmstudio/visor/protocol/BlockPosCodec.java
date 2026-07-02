package org.vmstudio.visor.protocol;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.value.VBlockPos;

public final class BlockPosCodec {
    private BlockPosCodec(){}

    private static final int PACKED_X_LENGTH = 26;
    private static final int PACKED_Z_LENGTH = 26;
    private static final int PACKED_Y_LENGTH = 64 - PACKED_X_LENGTH - PACKED_Z_LENGTH;
    private static final long PACKED_X_MASK = (1L << PACKED_X_LENGTH) - 1L;
    private static final long PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
    private static final long PACKED_Z_MASK = (1L << PACKED_Z_LENGTH) - 1L;
    private static final int Y_OFFSET = 0;
    private static final int Z_OFFSET = PACKED_Y_LENGTH;
    private static final int X_OFFSET = PACKED_Y_LENGTH + PACKED_Z_LENGTH;

    public static long pack(int x, int y, int z){
        long l = 0L;
        l |= (x & PACKED_X_MASK) << X_OFFSET;
        l |= (y & PACKED_Y_MASK) << Y_OFFSET;
        l |= (z & PACKED_Z_MASK) << Z_OFFSET;
        return l;
    }

    public static int unpackX(long packed){
        return (int) (packed << (64 - X_OFFSET - PACKED_X_LENGTH) >> (64 - PACKED_X_LENGTH));
    }

    public static int unpackY(long packed){
        return (int) (packed << (64 - Y_OFFSET - PACKED_Y_LENGTH) >> (64 - PACKED_Y_LENGTH));
    }

    public static int unpackZ(long packed){
        return (int) (packed << (64 - Z_OFFSET - PACKED_Z_LENGTH) >> (64 - PACKED_Z_LENGTH));
    }

    public static VisorBuf writeBlockPos(VisorBuf buf, VBlockPos pos){
        return buf.writeLong(pack(pos.x(), pos.y(), pos.z()));
    }

    public static VBlockPos readBlockPos(VisorBuf buf){
        long packed = buf.readLong();
        return new VBlockPos(unpackX(packed), unpackY(packed), unpackZ(packed));
    }
}

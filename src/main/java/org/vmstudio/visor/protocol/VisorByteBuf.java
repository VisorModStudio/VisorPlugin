package org.vmstudio.visor.protocol;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import org.vmstudio.visor.protocol.value.VBlockPos;

public final class VisorByteBuf {
    private byte[] buf;
    private int writerIndex;
    private int readerIndex;

    public VisorByteBuf(){
        this.buf = new byte[64];
    }

    public VisorByteBuf(byte[] data){
        this.buf = data;
        this.writerIndex = data.length;
    }

    private void ensure(int extra){
        int required = writerIndex + extra;
        if(required > buf.length){
            buf = Arrays.copyOf(buf, Math.max(buf.length * 2, required));
        }
    }

    public VisorByteBuf writeByte(int value){
        ensure(1);
        buf[writerIndex++] = (byte) value;
        return this;
    }

    public VisorByteBuf writeBoolean(boolean value){
        return writeByte(value ? 1 : 0);
    }

    public VisorByteBuf writeInt(int value){
        ensure(4);
        buf[writerIndex++] = (byte) (value >>> 24);
        buf[writerIndex++] = (byte) (value >>> 16);
        buf[writerIndex++] = (byte) (value >>> 8);
        buf[writerIndex++] = (byte) value;
        return this;
    }

    public VisorByteBuf writeVarInt(int value){
        while((value & ~0x7F) != 0){
            writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        writeByte(value);
        return this;
    }

    public VisorByteBuf writeLong(long value){
        ensure(8);
        for(int shift = 56; shift >= 0; shift -= 8){
            buf[writerIndex++] = (byte) (value >>> shift);
        }
        return this;
    }

    public VisorByteBuf writeFloat(float value){
        return writeInt(Float.floatToIntBits(value));
    }

    public VisorByteBuf writeUUID(UUID uuid){
        return writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits());
    }

    public VisorByteBuf writeBlockPos(VBlockPos pos){
        return writeLong(BlockPosCodec.pack(pos.x(), pos.y(), pos.z()));
    }

    public VisorByteBuf writeBytes(byte[] data){
        ensure(data.length);
        System.arraycopy(data, 0, buf, writerIndex, data.length);
        writerIndex += data.length;
        return this;
    }

    public VisorByteBuf writeStringTail(String value){
        return writeBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    public int readableBytes(){
        return writerIndex - readerIndex;
    }

    private void requireReadable(int n){
        if(readerIndex + n > writerIndex){
            throw new IndexOutOfBoundsException(
                    "Visor buffer underflow: need " + n + " byte(s), have " + readableBytes());
        }
    }

    public byte readByte(){
        requireReadable(1);
        return buf[readerIndex++];
    }

    public int readUnsignedByte(){
        return readByte() & 0xFF;
    }

    public boolean readBoolean(){
        return readByte() != 0;
    }

    public int readInt(){
        requireReadable(4);
        return ((buf[readerIndex++] & 0xFF) << 24)
                | ((buf[readerIndex++] & 0xFF) << 16)
                | ((buf[readerIndex++] & 0xFF) << 8)
                | (buf[readerIndex++] & 0xFF);
    }

    public int readVarInt(){
        int value = 0;
        int position = 0;
        byte b;
        do {
            b = readByte();
            value |= (b & 0x7F) << position;
            position += 7;
            if(position > 35){
                throw new IllegalArgumentException("VarInt too big");
            }
        }while((b & 0x80) != 0);
        return value;
    }

    public long readLong(){
        requireReadable(8);
        long result = 0L;
        for(int i = 0; i < 8; i++){
            result = (result << 8) | (buf[readerIndex++] & 0xFFL);
        }
        return result;
    }

    public float readFloat(){
        return Float.intBitsToFloat(readInt());
    }

    public UUID readUUID(){
        return new UUID(readLong(), readLong());
    }

    public VBlockPos readBlockPos(){
        long packed = readLong();
        return new VBlockPos(
                BlockPosCodec.unpackX(packed),
                BlockPosCodec.unpackY(packed),
                BlockPosCodec.unpackZ(packed));
    }

    public String readStringTail(){
        int len = readableBytes();
        String s = new String(buf, readerIndex, len, StandardCharsets.UTF_8);
        readerIndex += len;
        return s;
    }

    public byte[] toByteArray(){
        return Arrays.copyOf(buf, writerIndex);
    }
}

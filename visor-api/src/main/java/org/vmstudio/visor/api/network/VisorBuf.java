package org.vmstudio.visor.api.network;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * A buffer for reading and writing packet paylods
 * <p>
 * Create a buffer with {@link #create()}, wrap received bytes with
 * {@link #wrap(byte[])}, and extract written bytes with {@link #toBytes()}
 */
public final class VisorBuf {
    private byte[] buf;
    private int writerIndex;
    private int readerIndex;

    private VisorBuf(byte[] data, int writerIndex){
        this.buf = data;
        this.writerIndex = writerIndex;
    }

    /**
     * Create an empty buffer ready for writing
     *
     * @return a new writable buffer
     */
    public static VisorBuf create(){
        return new VisorBuf(new byte[64], 0);
    }

    /**
     * Wrap existing bytes for reading (does not copy)
     *
     * @param data the bytes to read from
     * @return a buffer positioned at the start of {@code data}
     */
    public static VisorBuf wrap(byte[] data){
        return new VisorBuf(data, data.length);
    }

    private void ensure(int extra){
        int required = writerIndex + extra;
        if(required > buf.length){
            buf = Arrays.copyOf(buf, Math.max(buf.length * 2, required));
        }
    }

    /**
     * Write a single byte (only the low 8 bits of {@code value} are used)
     *
     * @param value byte value
     * @return this buffer
     */
    public VisorBuf writeByte(int value){
        ensure(1);
        buf[writerIndex++] = (byte) value;
        return this;
    }

    /**
     * Write a boolean as a single byte ({@code 1} or {@code 0})
     *
     * @param value boolean value
     * @return this buffer
     */
    public VisorBuf writeBoolean(boolean value){
        return writeByte(value ? 1 : 0);
    }

    /**
     * Write a 32-bit int
     *
     * @param value int value
     * @return this buffer
     */
    public VisorBuf writeInt(int value){
        ensure(4);
        buf[writerIndex++] = (byte) (value >>> 24);
        buf[writerIndex++] = (byte) (value >>> 16);
        buf[writerIndex++] = (byte) (value >>> 8);
        buf[writerIndex++] = (byte) value;
        return this;
    }

    /**
     * Write a variable-length int (7 bits per byte)
     *
     * @param value int value
     * @return this buffer
     */
    public VisorBuf writeVarInt(int value){
        while((value & ~0x7F) != 0){
            writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        return writeByte(value);
    }

    /**
     * Write a 64-bit long
     *
     * @param value long value
     * @return this buffer
     */
    public VisorBuf writeLong(long value){
        ensure(8);
        for(int shift = 56; shift >= 0; shift -= 8){
            buf[writerIndex++] = (byte) (value >>> shift);
        }
        return this;
    }

    /**
     * Write a 32-bit float
     *
     * @param value float value
     * @return this buffer
     */
    public VisorBuf writeFloat(float value){
        return writeInt(Float.floatToIntBits(value));
    }

    /**
     * Write a 64-bit double
     *
     * @param value double value
     * @return this buffer
     */
    public VisorBuf writeDouble(double value){
        return writeLong(Double.doubleToLongBits(value));
    }

    /**
     * Write a UUID as two longs
     *
     * @param uuid uuid
     * @return this buffer
     */
    public VisorBuf writeUUID(UUID uuid){
        return writeLong(uuid.getMostSignificantBits()).writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Write a UTF-8 string
     *
     * @param value string
     * @return this buffer
     */
    public VisorBuf writeString(String value){
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        return writeVarInt(bytes.length).writeBytes(bytes);
    }

    /**
     * Write a byte array
     *
     * @param data bytes
     * @return this buffer
     */
    public VisorBuf writeByteArray(byte[] data){
        return writeVarInt(data.length).writeBytes(data);
    }

    /**
     * Write raw bytes with no length prefix
     *
     * @param data bytes
     * @return this buffer
     */
    public VisorBuf writeBytes(byte[] data){
        ensure(data.length);
        System.arraycopy(data, 0, buf, writerIndex, data.length);
        writerIndex += data.length;
        return this;
    }

    /**
     * @return the number of bytes remaining to read
     */
    public int readableBytes(){
        return writerIndex - readerIndex;
    }

    private void requireReadable(int n){
        if(readerIndex + n > writerIndex){
            throw new IndexOutOfBoundsException(
                    "Visor buffer underflow: need " + n + " bytes, have " + readableBytes());
        }
    }

    /**
     * Read a single signed byte written by {@link #writeByte}
     *
     * @return the byte value
     */
    public byte readByte(){
        requireReadable(1);
        return buf[readerIndex++];
    }

    /**
     * Read a single byte as an unsigned value
     *
     * @return the byte value in {@code 0..255}
     */
    public int readUnsignedByte(){
        return readByte() & 0xFF;
    }

    /**
     * Read a boolean (any non-zero byte is {@code true}) written by {@link #writeBoolean}
     *
     * @return the boolean value
     */
    public boolean readBoolean(){
        return readByte() != 0;
    }

    /**
     * Read a int written by {@link #writeInt}
     *
     * @return the int value
     */
    public int readInt(){
        requireReadable(4);
        return ((buf[readerIndex++] & 0xFF) << 24)
                | ((buf[readerIndex++] & 0xFF) << 16)
                | ((buf[readerIndex++] & 0xFF) << 8)
                | (buf[readerIndex++] & 0xFF);
    }

    /**
     * Read a variable-length int written by {@link #writeVarInt}
     *
     * @return the int value
     * @throws IllegalArgumentException if the encoding spans more than 5 bytes
     */
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
        } while((b & 0x80) != 0);
        return value;
    }

    /**
     * Read a long written by {@link #writeLong}
     *
     * @return the long value
     */
    public long readLong(){
        requireReadable(8);
        long result = 0L;
        for(int i = 0; i < 8; i++){
            result = (result << 8) | (buf[readerIndex++] & 0xFFL);
        }
        return result;
    }

    /**
     * Read a float written by {@link #writeFloat}
     *
     * @return the float value
     */
    public float readFloat(){
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Read a double written by {@link #writeDouble}
     *
     * @return the double value
     */
    public double readDouble(){
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Read a UUID written by {@link #writeUUID}
     *
     * @return the uuid
     */
    public UUID readUUID(){
        return new UUID(readLong(), readLong());
    }

    /**
     * Read a UTF-8 string written by {@link #writeString}
     *
     * @return the string
     */
    public String readString(){
        int len = readVarInt();
        requireReadable(len);
        String s = new String(buf, readerIndex, len, StandardCharsets.UTF_8);
        readerIndex += len;
        return s;
    }

    /**
     * Read a byte array written by {@link #writeByteArray}
     *
     * @return a copy of the bytes
     */
    public byte[] readByteArray(){
        int len = readVarInt();
        requireReadable(len);
        byte[] out = Arrays.copyOfRange(buf, readerIndex, readerIndex + len);
        readerIndex += len;
        return out;
    }

    /**
     * Read everything left in the buffer
     *
     * @return a copy of the remaining bytes
     */
    public byte[] readRemainingBytes(){
        byte[] out = Arrays.copyOfRange(buf, readerIndex, writerIndex);
        readerIndex = writerIndex;
        return out;
    }

    /**
     * Copy out the written bytes (from index {@code 0} to the writer position)
     *
     * @return the payload as a fresh array
     */
    public byte[] toBytes(){
        return Arrays.copyOf(buf, writerIndex);
    }
}

package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OffhandSlotIn(int slot) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OFFHAND_SLOT;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeInt(slot);
    }

    public static OffhandSlotIn read(VisorByteBuf buf){
        return new OffhandSlotIn(buf.readInt());
    }
}

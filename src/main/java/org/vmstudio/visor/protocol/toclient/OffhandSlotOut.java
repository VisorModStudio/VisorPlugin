package org.vmstudio.visor.protocol.toclient;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OffhandSlotOut(int slot) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OFFHAND_SLOT;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeInt(slot);
    }

    public static OffhandSlotOut read(VisorBuf buf){
        return new OffhandSlotOut(buf.readInt());
    }
}

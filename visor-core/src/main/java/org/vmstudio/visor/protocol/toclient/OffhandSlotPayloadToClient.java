package org.vmstudio.visor.protocol.toclient;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record OffhandSlotPayloadToClient(int slot) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OFFHAND_SLOT;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeInt(slot);
    }

    public static OffhandSlotPayloadToClient read(VisorByteBuf buf){
        return new OffhandSlotPayloadToClient(buf.readInt());
    }
}

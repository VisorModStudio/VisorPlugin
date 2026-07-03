package org.vmstudio.visorplugin.protocol.toclient;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

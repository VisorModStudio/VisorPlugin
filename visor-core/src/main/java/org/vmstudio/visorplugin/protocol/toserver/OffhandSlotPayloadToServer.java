package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OffhandSlotPayloadToServer(int slot) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OFFHAND_SLOT;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeInt(slot);
    }

    public static OffhandSlotPayloadToServer read(VisorByteBuf buf){
        return new OffhandSlotPayloadToServer(buf.readInt());
    }
}

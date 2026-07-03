package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record ActiveHandPayloadToServer(boolean activeHandMain) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.ACTIVE_HAND;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBoolean(activeHandMain);
    }

    public static ActiveHandPayloadToServer read(VisorByteBuf buf){
        return new ActiveHandPayloadToServer(buf.readBoolean());
    }
}

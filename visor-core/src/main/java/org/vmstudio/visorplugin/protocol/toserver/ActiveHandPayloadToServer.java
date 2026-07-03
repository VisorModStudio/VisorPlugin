package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

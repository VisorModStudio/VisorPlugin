package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record LeftHandedPayloadToServer(boolean leftHanded) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.LEFT_HANDED;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBoolean(leftHanded);
    }

    public static LeftHandedPayloadToServer read(VisorByteBuf buf){
        return new LeftHandedPayloadToServer(buf.readBoolean());
    }
}

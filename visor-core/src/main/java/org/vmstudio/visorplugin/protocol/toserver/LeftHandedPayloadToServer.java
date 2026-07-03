package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

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

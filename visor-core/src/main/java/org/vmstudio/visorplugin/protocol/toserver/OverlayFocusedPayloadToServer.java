package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record OverlayFocusedPayloadToServer(boolean overlayFocused) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OVERLAY_FOCUSED;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBoolean(overlayFocused);
    }

    public static OverlayFocusedPayloadToServer read(VisorByteBuf buf){
        return new OverlayFocusedPayloadToServer(buf.readBoolean());
    }
}

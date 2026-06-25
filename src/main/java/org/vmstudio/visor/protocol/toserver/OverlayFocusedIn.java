package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OverlayFocusedIn(boolean overlayFocused) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OVERLAY_FOCUSED;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeBoolean(overlayFocused);
    }

    public static OverlayFocusedIn read(VisorByteBuf buf){
        return new OverlayFocusedIn(buf.readBoolean());
    }
}

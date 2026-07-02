package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OverlayFocusedIn(boolean overlayFocused) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OVERLAY_FOCUSED;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBoolean(overlayFocused);
    }

    public static OverlayFocusedIn read(VisorBuf buf){
        return new OverlayFocusedIn(buf.readBoolean());
    }
}

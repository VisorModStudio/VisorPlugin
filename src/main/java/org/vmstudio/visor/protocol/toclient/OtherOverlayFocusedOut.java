package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record OtherOverlayFocusedOut(UUID playerUUID, boolean overlayFocused) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_OVERLAY_FOCUSED;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID).writeBoolean(overlayFocused);
    }

    public static OtherOverlayFocusedOut read(VisorByteBuf buf){
        return new OtherOverlayFocusedOut(buf.readUUID(), buf.readBoolean());
    }
}

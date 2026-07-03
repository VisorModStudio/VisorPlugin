package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;

public record SwingAttackPayloadToServer(int entityId, boolean shiftKeyDown, boolean mainHand) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.SWING_ATTACK;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeInt(entityId).writeBoolean(shiftKeyDown).writeBoolean(mainHand);
    }

    public static SwingAttackPayloadToServer read(VisorByteBuf buf){
        return new SwingAttackPayloadToServer(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }
}

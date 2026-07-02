package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record SwingAttackIn(int entityId, boolean shiftKeyDown, boolean mainHand) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.SWING_ATTACK;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeInt(entityId).writeBoolean(shiftKeyDown).writeBoolean(mainHand);
    }

    public static SwingAttackIn read(VisorBuf buf){
        return new SwingAttackIn(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }
}

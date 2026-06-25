package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.PoseData;

public record PoseDataIn(PoseData pose) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.POSE_DATA;
    }

    @Override
    public void write(VisorByteBuf buf){
        pose.write(buf);
    }

    public static PoseDataIn read(VisorByteBuf buf){
        return new PoseDataIn(PoseData.read(buf));
    }
}

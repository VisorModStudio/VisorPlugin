package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.PoseData;

public record OtherPoseDataOut(UUID playerUUID, PoseData pose) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_POSE_DATA;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        pose.write(buf);
    }

    public static OtherPoseDataOut read(VisorByteBuf buf){
        return new OtherPoseDataOut(buf.readUUID(), PoseData.read(buf));
    }
}

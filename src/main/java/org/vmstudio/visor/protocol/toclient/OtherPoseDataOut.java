package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorOutbound;
import org.vmstudio.visor.protocol.VisorPayloadId;
import org.vmstudio.visor.protocol.value.PoseData;

public record OtherPoseDataOut(UUID playerUUID, PoseData pose) implements VisorOutbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.OTHER_VR_POSE_DATA;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeUUID(playerUUID);
        pose.write(buf);
    }

    public static OtherPoseDataOut read(VisorBuf buf){
        return new OtherPoseDataOut(buf.readUUID(), PoseData.read(buf));
    }
}

package org.vmstudio.visor.protocol.toclient;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToClient;
import org.vmstudio.visor.protocol.VisorCorePayloadId;
import org.vmstudio.visor.api.network.buffer.PoseDataBuffer;

public record OtherPoseDataPayloadToClient(UUID playerUUID, PoseDataBuffer pose) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_POSE_DATA;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        pose.serialize(buf);
    }

    public static OtherPoseDataPayloadToClient read(VisorByteBuf buf){
        return new OtherPoseDataPayloadToClient(buf.readUUID(), PoseDataBuffer.deserialize(buf));
    }
}

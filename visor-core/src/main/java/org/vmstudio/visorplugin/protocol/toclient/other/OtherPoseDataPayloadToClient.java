package org.vmstudio.visorplugin.protocol.toclient.other;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToClient;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;
import org.vmstudio.visorplugin.api.network.buffer.PoseDataBuffer;

public record OtherPoseDataPayloadToClient(UUID playerUUID, PoseDataBuffer pose) implements VisorCorePayloadToClient {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.OTHER_VR_POSE_DATA;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeUUID(playerUUID);
        writeSimple(buf);
    }

    public void writeSimple(VisorByteBuf buf){
        pose.serialize(buf);
    }

    public static OtherPoseDataPayloadToClient read(VisorByteBuf buf){
        UUID uuid = buf.readUUID();
        return readSimple(uuid, buf);
    }

    public static OtherPoseDataPayloadToClient readSimple(UUID uuid, VisorByteBuf buf){
        return new OtherPoseDataPayloadToClient(uuid, PoseDataBuffer.deserialize(buf));
    }
}

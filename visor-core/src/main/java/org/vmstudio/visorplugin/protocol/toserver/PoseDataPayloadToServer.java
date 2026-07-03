package org.vmstudio.visorplugin.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadToServer;
import org.vmstudio.visorplugin.protocol.VisorCorePayloadId;
import org.vmstudio.visorplugin.api.network.buffer.PoseDataBuffer;

public record PoseDataPayloadToServer(PoseDataBuffer pose) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.POSE_DATA;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        pose.serialize(buf);
    }

    public static PoseDataPayloadToServer read(VisorByteBuf buf){
        return new PoseDataPayloadToServer(PoseDataBuffer.deserialize(buf));
    }
}

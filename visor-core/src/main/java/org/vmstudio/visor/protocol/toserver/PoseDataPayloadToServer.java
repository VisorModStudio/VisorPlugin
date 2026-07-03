package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;
import org.vmstudio.visor.api.network.buffer.PoseDataBuffer;

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

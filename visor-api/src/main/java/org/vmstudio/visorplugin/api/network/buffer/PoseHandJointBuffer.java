package org.vmstudio.visorplugin.api.network.buffer;

import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.vmstudio.visorplugin.api.player.VRHandJointType;

public record PoseHandJointBuffer(VRHandJointType type,
                                  Vector3fc position,
                                  Quaternionfc orientation) implements VRDataBuffer{
    @Override
    public void serialize(VisorByteBuf byteBuf) {
        PoseElementBuffer.serializeVec(byteBuf, this.position);
        PoseElementBuffer.serializeQuat(byteBuf, this.orientation);
    }

    public static PoseHandJointBuffer deserialize(VRHandJointType type,
                                                  VisorByteBuf byteBuf) {
        return new PoseHandJointBuffer(
                type,
                PoseElementBuffer.deserializeVec(byteBuf),
                PoseElementBuffer.deserializeVRQuaternion(byteBuf)
        );
    }
}

package org.vmstudio.visor.api.network.buffer;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vmstudio.visor.api.player.VRBodyPartType;

public record PoseElementBuffer(VRBodyPartType type,
                                Vector3fc position,
                                Quaternionfc orientation) implements VRDataBuffer{
    @Override
    public void serialize(VisorByteBuf buffer) {
        serializeVec(buffer, this.position);
        serializeQuat(buffer, this.orientation);
    }

    public static PoseElementBuffer deserialize(VRBodyPartType type,
                                                VisorByteBuf byteBuf) {
        return new PoseElementBuffer(
                type,
                deserializeVec(byteBuf),
                deserializeVRQuaternion(byteBuf)
        );
    }

    public static PoseElementBuffer createEmpty(VRBodyPartType type){
        return new PoseElementBuffer(type, new Vector3f(), new Quaternionf());
    }
    public static void serializeVec(VisorByteBuf buffer, Vector3fc vec3) {
        buffer.writeFloat(vec3.x());
        buffer.writeFloat(vec3.y());
        buffer.writeFloat(vec3.z());
    }

    public static void serializeQuat(VisorByteBuf buffer, Quaternionfc quat) {
        buffer.writeFloat(quat.x());
        buffer.writeFloat(quat.y());
        buffer.writeFloat(quat.z());
        buffer.writeFloat(quat.w());
    }


    public static Vector3f deserializeVec(VisorByteBuf buffer) {
        return new Vector3f(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        );
    }

    public static Quaternionf deserializeVRQuaternion(VisorByteBuf buffer) {
        return new Quaternionf(
                buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),
                buffer.readFloat()
        );
    }
}

package org.vmstudio.visorplugin.api.network.buffer;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visorplugin.api.player.VRHandJointType;

public record PoseHandBuffer(@NotNull List<PoseHandJointBuffer> joints) implements VRDataBuffer{

    @Override
    public void serialize(VisorByteBuf byteBuf) {
        PoseHandJointBuffer[] byIndex = new PoseHandJointBuffer[VRHandJointType.COUNT];
        int bitMask = 0;
        for (PoseHandJointBuffer joint : joints) {
            int i = joint.type().ordinal();
            byIndex[i] = joint;
            bitMask |= 1 << i;
        }
        byteBuf.writeVarInt(bitMask);
        for (PoseHandJointBuffer joint : byIndex) {
            if (joint != null) {
                joint.serialize(byteBuf);
            }
        }
    }

    public static PoseHandBuffer deserialize(VisorByteBuf byteBuf) {
        int bitMask = byteBuf.readVarInt();
        List<PoseHandJointBuffer> joints = new ArrayList<>();
        for (int i = 0; i < VRHandJointType.COUNT; i++) {
            if ((bitMask & (1 << i)) != 0) {
                joints.add(PoseHandJointBuffer.deserialize(VRHandJointType.fromIndex(i), byteBuf));
            }
        }
        return new PoseHandBuffer(joints);
    }

    public static PoseHandBuffer createEmpty() {
        return new PoseHandBuffer(List.of());
    }

    public boolean isEmpty() {
        return joints.isEmpty();
    }

    public @Nullable PoseHandJointBuffer getJoint(@NotNull VRHandJointType type) {
        for (PoseHandJointBuffer joint : joints) {
            if (joint.type() == type) {
                return joint;
            }
        }
        return null;
    }
}

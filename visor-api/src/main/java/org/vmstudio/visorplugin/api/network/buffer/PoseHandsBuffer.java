package org.vmstudio.visorplugin.api.network.buffer;

import org.jetbrains.annotations.NotNull;

public record PoseHandsBuffer(@NotNull PoseHandBuffer leftHand,
                              @NotNull PoseHandBuffer rightHand) implements VRDataBuffer{

    @Override
    public void serialize(VisorByteBuf byteBuf) {
        this.leftHand.serialize(byteBuf);
        this.rightHand.serialize(byteBuf);
    }

    public static PoseHandsBuffer deserialize(VisorByteBuf byteBuf) {
        return new PoseHandsBuffer(
                PoseHandBuffer.deserialize(byteBuf),
                PoseHandBuffer.deserialize(byteBuf)
        );
    }

    public static PoseHandsBuffer createEmpty() {
        return new PoseHandsBuffer(
                PoseHandBuffer.createEmpty(),
                PoseHandBuffer.createEmpty()
        );
    }

    public boolean isEmpty() {
        return leftHand.isEmpty() && rightHand.isEmpty();
    }
}

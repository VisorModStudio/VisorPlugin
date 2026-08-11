package org.vmstudio.visorplugin.api.network.buffer;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.player.VRBodyPartType;

public record PoseDataBuffer(@NotNull PoseElementBuffer hmd,
                             @NotNull PoseElementBuffer mainHand,
                             @NotNull PoseElementBuffer offhand,
                             @NotNull PoseTrackersBuffer trackers,
                             @NotNull PoseHandsBuffer hands) implements VRDataBuffer{


    @Override
    public void serialize(VisorByteBuf buffer) {
        this.hmd.serialize(buffer);
        this.mainHand.serialize(buffer);
        this.offhand.serialize(buffer);
        this.trackers.serialize(buffer);
        this.hands.serialize(buffer);
    }


    public static PoseDataBuffer deserialize(VisorByteBuf byteBuf) {
        PoseElementBuffer hmd = PoseElementBuffer.deserialize(VRBodyPartType.HEAD, byteBuf);
        PoseElementBuffer mainHand = PoseElementBuffer.deserialize(VRBodyPartType.MAIN_HAND, byteBuf);
        PoseElementBuffer offhand = PoseElementBuffer.deserialize(VRBodyPartType.OFFHAND, byteBuf);
        PoseTrackersBuffer trackers = PoseTrackersBuffer.deserialize(byteBuf);
        PoseHandsBuffer hands = PoseHandsBuffer.deserialize(byteBuf);
        return new PoseDataBuffer(hmd, mainHand, offhand, trackers, hands);
    }


}

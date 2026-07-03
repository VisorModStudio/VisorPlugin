package org.vmstudio.visorplugin.api.network.buffer;

import org.jetbrains.annotations.Nullable;
import org.vmstudio.visorplugin.api.player.VRBodyPartType;

public record PoseTrackersBuffer(@Nullable PoseElementBuffer waist,
                                 @Nullable PoseElementBuffer chest,

                                 @Nullable PoseElementBuffer leftFoot,
                                 @Nullable PoseElementBuffer rightFoot,
                                 @Nullable PoseElementBuffer leftAnkle,
                                 @Nullable PoseElementBuffer rightAnkle,
                                 @Nullable PoseElementBuffer leftKnee,
                                 @Nullable PoseElementBuffer rightKnee,

                                 @Nullable PoseElementBuffer leftWrist,
                                 @Nullable PoseElementBuffer rightWrist,
                                 @Nullable PoseElementBuffer leftElbow,
                                 @Nullable PoseElementBuffer rightElbow,
                                 @Nullable PoseElementBuffer leftShoulder,
                                 @Nullable PoseElementBuffer rightShoulder) implements VRDataBuffer{

    private static final VRBodyPartType[] TYPES = {
            VRBodyPartType.WAIST,
            VRBodyPartType.CHEST,

            VRBodyPartType.LEFT_FOOT,
            VRBodyPartType.RIGHT_FOOT,
            VRBodyPartType.LEFT_ANKLE,
            VRBodyPartType.RIGHT_ANKLE,
            VRBodyPartType.LEFT_KNEE,
            VRBodyPartType.RIGHT_KNEE,

            VRBodyPartType.LEFT_WRIST,
            VRBodyPartType.RIGHT_WRIST,
            VRBodyPartType.LEFT_ELBOW,
            VRBodyPartType.RIGHT_ELBOW,
            VRBodyPartType.LEFT_SHOULDER,
            VRBodyPartType.RIGHT_SHOULDER
    };

    @Override
    public void serialize(VisorByteBuf buffer) {
        PoseElementBuffer[] elements = {
                waist, chest, leftFoot, rightFoot, leftAnkle, rightAnkle, leftKnee,
                rightKnee, leftWrist, rightWrist, leftElbow, rightElbow, leftShoulder, rightShoulder
        };
        int bitMask = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                bitMask |= 1 << i;
            }
        }
        buffer.writeVarInt(bitMask);
        for (PoseElementBuffer element : elements) {
            if (element != null) {
                element.serialize(buffer);
            }
        }
    }

    public static PoseTrackersBuffer deserialize(VisorByteBuf byteBuf) {
        int bitMask = byteBuf.readVarInt();
        PoseElementBuffer[] e = new PoseElementBuffer[TYPES.length];
        for (int i = 0; i < e.length; i++) {
            if ((bitMask & (1 << i)) != 0) {
                e[i] = PoseElementBuffer.deserialize(TYPES[i], byteBuf);
            }
        }
        return new PoseTrackersBuffer(
                e[0], e[1], e[2], e[3], e[4], e[5], e[6],
                e[7], e[8], e[9], e[10], e[11], e[12], e[13]
        );
    }


    public static PoseTrackersBuffer createEmpty() {
        return new PoseTrackersBuffer(
                null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null
        );
    }

    public PoseElementBuffer[] toArray(){
        return new PoseElementBuffer[]{
                waist, chest, leftFoot, rightFoot, leftAnkle, rightAnkle, leftKnee,
                rightKnee, leftWrist, rightWrist, leftElbow, rightElbow, leftShoulder, rightShoulder
        };
    }
}

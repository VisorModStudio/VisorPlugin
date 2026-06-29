package org.vmstudio.visor.protocol.value;

import org.vmstudio.visor.protocol.VisorByteBuf;

public record PoseData(PoseElement hmd, PoseElement mainHand, PoseElement offhand, PoseTrackers trackers){
    public void write(VisorByteBuf buf){
        hmd.write(buf);
        mainHand.write(buf);
        offhand.write(buf);
        trackers.write(buf);
    }

    public static PoseData read(VisorByteBuf buf){
        return new PoseData(
                PoseElement.read(buf), PoseElement.read(buf), PoseElement.read(buf),
                PoseTrackers.read(buf));
    }
}

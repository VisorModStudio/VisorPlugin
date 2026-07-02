package org.vmstudio.visor.protocol.value;

import org.vmstudio.visor.api.network.VisorBuf;

public record PoseData(PoseElement hmd, PoseElement mainHand, PoseElement offhand, PoseTrackers trackers){
    public void write(VisorBuf buf){
        hmd.write(buf);
        mainHand.write(buf);
        offhand.write(buf);
        trackers.write(buf);
    }

    public static PoseData read(VisorBuf buf){
        return new PoseData(
                PoseElement.read(buf), PoseElement.read(buf), PoseElement.read(buf),
                PoseTrackers.read(buf));
    }
}

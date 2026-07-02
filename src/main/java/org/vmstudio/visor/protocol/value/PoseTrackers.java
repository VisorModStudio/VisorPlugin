package org.vmstudio.visor.protocol.value;

import org.vmstudio.visor.api.network.VisorBuf;

public record PoseTrackers(
        PoseElement waist, PoseElement chest,
        PoseElement leftFoot, PoseElement rightFoot,
        PoseElement leftAnkle, PoseElement rightAnkle,
        PoseElement leftKnee, PoseElement rightKnee,
        PoseElement leftWrist, PoseElement rightWrist,
        PoseElement leftElbow, PoseElement rightElbow,
        PoseElement leftShoulder, PoseElement rightShoulder){

    public static PoseTrackers empty(){
        return new PoseTrackers(null, null, null, null, null, null, null,
                null, null, null, null, null, null, null);
    }

    public PoseElement[] toArray(){
        return ordered();
    }

    private PoseElement[] ordered(){
        return new PoseElement[]{
                waist, chest, leftFoot, rightFoot, leftAnkle, rightAnkle, leftKnee,
                rightKnee, leftWrist, rightWrist, leftElbow, rightElbow, leftShoulder, rightShoulder
        };
    }

    public void write(VisorBuf buf){
        PoseElement[] e = ordered();
        int bitMask = 0;
        for(int i = 0; i < e.length; i++){
            if(e[i] != null){
                bitMask |= 1 << i;
            }
        }
        buf.writeVarInt(bitMask);
        for(PoseElement el : e){
            if(el != null){
                el.write(buf);
            }
        }
    }

    public static PoseTrackers read(VisorBuf buf){
        if(buf.readableBytes() <= 0){
            return empty();
        }
        int bitMask = buf.readVarInt();
        PoseElement[] e = new PoseElement[14];
        for(int i = 0; i < e.length; i++){
            if((bitMask & (1 << i)) != 0){
                e[i] = PoseElement.read(buf);
            }
        }
        return new PoseTrackers(
                e[0], e[1], e[2], e[3], e[4], e[5], e[6],
                e[7], e[8], e[9], e[10], e[11], e[12], e[13]);
    }
}

package org.vmstudio.visor.api.player;

import org.jetbrains.annotations.NotNull;

/** Which VR controller hand */
public enum HandType {
    MAIN,
    OFFHAND;

    public @NotNull HandType opposite(){
        return this == MAIN ? OFFHAND : MAIN;
    }

    public @NotNull VRBodyPartType asBodyPart(){
        return this == MAIN ? VRBodyPartType.MAIN_HAND : VRBodyPartType.OFFHAND;
    }

    public static @NotNull HandType fromInt(int id){
        return id == 0 ? MAIN : OFFHAND;
    }
}

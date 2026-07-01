package org.vmstudio.visor.common.settings;

import org.vmstudio.visor.api.player.SupportedMovement;

public record VisorSettings(
        boolean serverDebug,
        boolean vrOnly,
        boolean twoHandedVR,
        boolean betterSwinging,
        long swingingRepairDelay,
        boolean roomCrawlingSupported,
        boolean roomClimbingSupported,
        boolean pvpVRvsVanilla,
        boolean pvpVRvsVR,
        boolean notifyPvpBlocked,
        double creeperSwellDistance,
        SupportedMovement supportedMovement,
        int teleportUpLimit,
        int teleportDownLimit,
        int teleportForwardLimit,
        boolean trackersSupported){

    public static VisorSettings defaults(){
        return new VisorSettings(
                false,
                false,
                false,
                false,
                400L,
                false,
                false,
                true,
                true,
                false,
                1.75,
                SupportedMovement.CONTROLLER,
                1,
                4,
                16,
                false);
    }

    public String toClientYaml(){
        return "vrOnly: " + vrOnly + '\n'
                + "serverDebug: " + serverDebug + '\n'
                + "twoHandedVR: " + twoHandedVR + '\n'
                + "betterSwinging: " + betterSwinging + '\n'
                + "roomCrawlingSupported: " + roomCrawlingSupported + '\n'
                + "roomClimbingSupported: " + roomClimbingSupported + '\n'
                + "pvpVRvsVanilla: " + pvpVRvsVanilla + '\n'
                + "pvpVRvsVR: " + pvpVRvsVR + '\n'
                + "notifyPvpBlocked: " + notifyPvpBlocked + '\n'
                + "creeperSwellDistance: " + creeperSwellDistance + '\n'
                + "supportedMovement: " + supportedMovement.name() + '\n'
                + "trackersSupported: " + trackersSupported + '\n';
    }
}

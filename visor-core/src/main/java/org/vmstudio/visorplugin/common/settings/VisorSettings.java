package org.vmstudio.visorplugin.common.settings;

import org.vmstudio.visorplugin.api.player.SupportedMovement;

public record VisorSettings(
        boolean serverDebug,
        boolean vrOnly,
        boolean twoHandedVR,
        boolean betterSwinging,
        boolean roomscaleShieldBlocking,
        boolean attacksWhileBlocking,
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
        boolean bodyTrackersSupported,
        boolean handTrackersSupported,
        boolean bodyTrackersSendToOthers,
        boolean handTrackersSendToOthers){

    public static VisorSettings defaults(){
        return new VisorSettings(
                false,
                false,
                false,
                false,
                false,
                true,
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
                false,
                false,
                true,
                true);
    }


    public String toClientYaml(){
        return "vrOnly: " + vrOnly + '\n'
                + "serverDebug: " + serverDebug + '\n'
                + "twoHandedVR: " + twoHandedVR + '\n'
                + "betterSwinging: " + betterSwinging + '\n'
                + "roomscaleShieldBlocking: " + roomscaleShieldBlocking + '\n'
                + "attacksWhileBlocking: " + attacksWhileBlocking + '\n'
                + "roomCrawlingSupported: " + roomCrawlingSupported + '\n'
                + "roomClimbingSupported: " + roomClimbingSupported + '\n'
                + "pvpVRvsVanilla: " + pvpVRvsVanilla + '\n'
                + "pvpVRvsVR: " + pvpVRvsVR + '\n'
                + "notifyPvpBlocked: " + notifyPvpBlocked + '\n'
                + "creeperSwellDistance: " + creeperSwellDistance + '\n'
                + "supportedMovement: " + supportedMovement.name() + '\n'
                + "teleportUpLimit: " + teleportUpLimit + '\n'
                + "teleportDownLimit: " + teleportDownLimit + '\n'
                + "teleportForwardLimit: " + teleportForwardLimit + '\n'
                + "bodyTrackersSupported: " + bodyTrackersSupported + '\n'
                + "handTrackersSupported: " + handTrackersSupported + '\n';
    }
}

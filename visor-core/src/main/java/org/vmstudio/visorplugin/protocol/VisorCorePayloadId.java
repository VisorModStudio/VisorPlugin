package org.vmstudio.visorplugin.protocol;

/**
 * Synced Payload ids from Visor mod
 */
public enum VisorCorePayloadId {
    //TO BOTH
    HANDSHAKE,
    ROTATION_Y,
    OFFHAND_SLOT,

    //TO CLIENT
    SERVER_SETTINGS,
    BLOCK_DAMAGE,
    OTHER_VR_POSE_DATA,
    OTHER_VR_LEFT_HANDED,
    OTHER_VR_BODY_TYPE,
    OTHER_VR_WORLD_SCALE,
    OTHER_GUN_ANGLE,
    OTHER_VR_FULL_HEIGHT,

    //TO SERVER
    FULL_HEIGHT,
    POSE_DATA,
    VR_BODY_TYPE,
    LEFT_HANDED,
    ACTIVE_HAND,
    WORLD_SCALE,
    GUN_ANGLE,
    CRAWLING,
    CLIMBING,
    TELEPORT,
    SWING_ATTACK,
    SWING_BLOCK,
    OVERLAY_FOCUSED,
    OTHER_VR_OVERLAY_FOCUSED;




    public byte byteOrdinal(){
        return (byte) ordinal();
    }

    public static VisorCorePayloadId fromOrdinal(byte ordinalByte){
        // -127...127 to 0..255
        int unsignedByte = ordinalByte & 0xFF;
        return unsignedByte < values().length ? values()[unsignedByte] : null;
    }
}

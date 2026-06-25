package org.vmstudio.visor.protocol;

/**
 * {@code ordinal()} is the wire byte and must stay in exact sync with the mod's
 * {@code VisorCorePayloadID}
 */
public enum VisorPayloadId {
    HANDSHAKE,
    ROTATION_Y,
    OFFHAND_SLOT,

    SERVER_SETTINGS,
    BLOCK_DAMAGE,
    OTHER_VR_POSE_DATA,
    OTHER_VR_LEFT_HANDED,
    OTHER_VR_BODY_TYPE,
    OTHER_VR_WORLD_SCALE,
    OTHER_GUN_ANGLE,
    OTHER_VR_FULL_HEIGHT,

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

    private static final VisorPayloadId[] VALUES = values();

    public byte byteOrdinal(){
        return (byte) ordinal();
    }

    public static VisorPayloadId fromByte(byte b){
        int i = b & 0xFF;
        return i < VALUES.length ? VALUES[i] : null;
    }
}

package org.vmstudio.visor.common.platform;

import java.util.Set;
import java.util.UUID;

import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.value.PoseData;
import org.vmstudio.visor.protocol.value.VBlockPos;

public interface PlatformPlayer {
    UUID uuid();

    String name();

    boolean isOp();

    boolean isOnline();

    void sendPayload(byte[] channelData);

    void disconnect(String reason);

    void teleportAbsolute(double x, double y, double z);

    void resetFallDistance();

    void setForcedCrawl(boolean crawling);

    void swingAttack(int entityId, boolean shiftKeyDown, boolean mainHand);

    void blockSwing(VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks);

    void tickEffects();

    void swellNearbyCreepers(double distance, double hmdOffsetX, double hmdOffsetY, double hmdOffsetZ);

    void showTrackerDebug(PoseData pose);

    void clearTrackerDebug();

    Set<PlatformPlayer> trackers();
}

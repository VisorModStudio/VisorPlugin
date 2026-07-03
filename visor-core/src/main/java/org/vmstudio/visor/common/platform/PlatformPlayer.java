package org.vmstudio.visor.common.platform;

import java.util.Set;
import java.util.UUID;

import org.vmstudio.visor.api.network.buffer.DirectionValue;
import org.vmstudio.visor.api.network.buffer.PoseDataBuffer;
import org.vmstudio.visor.api.network.buffer.VBlockPos;

public interface PlatformPlayer {
    UUID uuid();

    String name();

    boolean isOp();

    boolean isOnline();

    double x();

    double y();

    double z();

    void sendPayload(byte[] channelData);

    void disconnect(String reason);

    void teleportAbsolute(double x, double y, double z);

    void resetFallDistance();

    void setForcedCrawl(boolean crawling);

    void swingAttack(int entityId, boolean shiftKeyDown, boolean mainHand);

    void blockSwing(VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks);

    void tickEffects();

    void swellNearbyCreepers(double distance, double hmdOffsetX, double hmdOffsetY, double hmdOffsetZ);

    void showTrackerDebug(PoseDataBuffer pose);

    void clearTrackerDebug();

    Set<PlatformPlayer> trackers();
}

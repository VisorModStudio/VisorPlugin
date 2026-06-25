package org.vmstudio.visor.nms;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.value.VBlockPos;

public interface VersionAdapter {
    String name();

    int priority();

    boolean supports(McVersion version);

    boolean selfTest();

    default void init(Plugin plugin){
    }

    default void tickEffects(Player player){
    }

    boolean forceCrawlPose(Player player, boolean crawling);

    void swingAttack(Player attacker, int entityId, boolean shiftKeyDown, boolean mainHand);

    void blockSwing(Player player, VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks);

    default void onPlayerQuit(UUID uuid){}

    Set<Player> trackers(Player player);
}

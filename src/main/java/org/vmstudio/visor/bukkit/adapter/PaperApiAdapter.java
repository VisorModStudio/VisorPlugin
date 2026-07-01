package org.vmstudio.visor.bukkit.adapter;

import java.util.Set;

import org.bukkit.entity.Player;
import org.vmstudio.visor.common.platform.VisorLogger;
import org.vmstudio.visor.api.nms.McVersion;
import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.value.VBlockPos;

public final class PaperApiAdapter implements VisorVersionAdapter {
    private static final double TRACKER_FALLBACK_RANGE = 256.0;

    private final VisorLogger logger;
    private boolean warnedSwingAttack;
    private boolean warnedBlockSwing;

    public PaperApiAdapter(VisorLogger logger){
        this.logger = logger;
    }

    @Override
    public String name(){
        return "paper-api";
    }

    @Override
    public int priority(){
        return 100;
    }

    @Override
    public boolean supports(McVersion version){
        return true;
    }

    @Override
    public boolean selfTest(){
        return true;
    }

    @Override
    public boolean forceCrawlPose(Player player, boolean crawling){
        try {
            player.setSwimming(crawling);
            return true;
        }catch (Throwable t){
            return false;
        }
    }

    @Override
    public void swingAttack(Player attacker, int entityId, boolean shiftKeyDown, boolean mainHand){
        if(!warnedSwingAttack){
            warnedSwingAttack = true;
            logger.warn("betterSwinging attack-by-id is degraded on this server "
                    + "melee VR swings fall back to vanilla");
        }
    }

    @Override
    public void blockSwing(Player player, VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks){
        if(!warnedBlockSwing){
            warnedBlockSwing = true;
            logger.warn("betterSwinging block-break is degraded on this server "
                    + "VR mining falls back to vanilla");
        }
    }

    @Override
    public Set<Player> trackers(Player player){
        return Trackers.of(player, TRACKER_FALLBACK_RANGE);
    }
}

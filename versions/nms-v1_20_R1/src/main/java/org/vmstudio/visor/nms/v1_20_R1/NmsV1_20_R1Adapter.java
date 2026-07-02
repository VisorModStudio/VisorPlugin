package org.vmstudio.visor.nms.v1_20_R1;

import static net.minecraft.server.network.ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.vmstudio.visor.api.nms.McVersion;
import org.vmstudio.visor.bukkit.adapter.VisorVersionAdapter;
import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.VisorCodec;
import org.vmstudio.visor.api.VisorProtocol;
import org.vmstudio.visor.protocol.toclient.BlockDamageOut;
import org.vmstudio.visor.protocol.value.VBlockPos;

public final class NmsV1_20_R1Adapter implements VisorVersionAdapter {
    private static final Logger LOG = Logger.getLogger("VisorPlugin/nms-v1_20_R1");

    private Plugin plugin;
    private final Map<UUID, Map<Long, Progress>> blockDamage = new ConcurrentHashMap<>();

    private boolean degradedSwingAttack;
    private boolean degradedBlockSwing;
    private boolean degradedCrawl;
    private boolean degradedTick;

    public NmsV1_20_R1Adapter(){}

    @Override
    public String name(){
        return "nms-v1_20_R1";
    }

    @Override
    public int priority(){
        return 1000;
    }

    @Override
    public boolean supports(McVersion version){
        return version.major() == 1 && version.minor() == 20 && version.patch() == 1;
    }

    @Override
    public boolean selfTest(){
        try {
            Class.forName("org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer");
            return true;
        }catch (Throwable t){
            return false;
        }
    }

    @Override
    public void init(Plugin plugin){
        this.plugin = plugin;
    }

    private static ServerPlayer handle(Player player){
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public boolean forceCrawlPose(Player player, boolean crawling){
        try {
            handle(player).setPose(crawling ? Pose.SWIMMING : Pose.STANDING);
            return true;
        }catch (Throwable t){
            if(!degradedCrawl){
                degradedCrawl = true;
                LOG.log(Level.WARNING, "forceCrawlPose failed; degrading. Verify the reobf mapping.", t);
            }
            return false;
        }
    }

    @Override
    public void swingAttack(Player attacker, int entityId, boolean shiftKeyDown, boolean mainHand){
        try {
            swingAttack0(attacker, entityId, shiftKeyDown, mainHand);
        }catch (Throwable t){
            if(!degradedSwingAttack){
                degradedSwingAttack = true;
                LOG.log(Level.WARNING, "swingAttack failed; degrading. Verify the reobf mapping.", t);
            }
        }
    }

    private void swingAttack0(Player attacker, int entityId, boolean shiftKeyDown, boolean mainHand){
        ServerPlayer sp = handle(attacker);
        ServerLevel level = sp.serverLevel();
        InteractionHand hand = mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        Entity target = level.getEntityOrPart(entityId);
        sp.resetLastActionTime();
        sp.setShiftKeyDown(shiftKeyDown);
        if(target == null){
            return;
        }
        if(!level.getWorldBorder().isWithinBounds(target.blockPosition())){
            return;
        }
        AABB box = target.getBoundingBox();
        if(box.distanceToSqr(sp.getEyePosition()) >= MAX_INTERACTION_DISTANCE){
            return;
        }
        if(target instanceof ItemEntity || target instanceof ExperienceOrb
                || target instanceof AbstractArrow || target == sp){
            return;
        }
        ItemStack item = sp.getItemInHand(hand);
        if(!item.isItemEnabled(level.enabledFeatures())){
            return;
        }
        if(sp.gameMode.getGameModeForPlayer() == GameType.SPECTATOR){
            sp.setCamera(target);
            return;
        }
        sp.attack(target);
        sp.swing(hand);
    }

    @Override
    public void blockSwing(Player player, VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks){
        try {
            blockSwing0(player, pos, mainHand, sequence, repairDelayTicks);
        }catch (Throwable t){
            if(!degradedBlockSwing){
                degradedBlockSwing = true;
                LOG.log(Level.WARNING, "blockSwing failed; degrading. Verify the reobf mapping.", t);
            }
        }
    }

    @Override
    public void onPlayerQuit(UUID uuid){
        blockDamage.remove(uuid);
    }

    private void blockSwing0(Player player, VBlockPos pos, boolean mainHand, int sequence, long repairDelayTicks){
        ServerPlayer sp = handle(player);
        ServerLevel level = sp.serverLevel();
        BlockPos nmsPos = new BlockPos(pos.x(), pos.y(), pos.z());
        InteractionHand hand = mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        ItemStack item = sp.getItemInHand(hand);
        try {
            if(sp.getEyePosition().distanceToSqr(Vec3.atCenterOf(nmsPos)) > MAX_INTERACTION_DISTANCE){
                return;
            }
            if(nmsPos.getY() >= level.getMaxBuildHeight() || !level.mayInteract(sp, nmsPos)){
                return;
            }
            if(sp.gameMode.getGameModeForPlayer() == GameType.CREATIVE){
                clearProgress(sp, level, nmsPos);
                sp.gameMode.destroyBlock(nmsPos);
                return;
            }

            BlockState state = level.getBlockState(nmsPos);
            float damage = 1.0F;
            if(!state.isAir()){
                state.attack(level, nmsPos, sp);
                Progress saved = mapFor(sp).get(nmsPos.asLong());
                damage = getDestroyProgress(state, sp, level, nmsPos, item)
                        + (saved != null ? saved.damage : 0F);
            }

            if(!state.isAir() && damage >= 1.0F){
                clearProgress(sp, level, nmsPos);
                sp.gameMode.destroyBlock(nmsPos);
            }else{
                int stage = (int) (damage * 10.0F);
                level.destroyBlockProgress(sp.getId(), nmsPos, stage);
                sp.connection.send(new ClientboundBlockDestructionPacket(sp.getId(), nmsPos, stage));
                broadcastBlockDamage(sp, nmsPos, stage);

                Progress p = mapFor(sp).computeIfAbsent(nmsPos.asLong(), k -> new Progress());
                p.delay = repairDelayTicks;
                p.damage = damage;
            }
        }finally {
            try {
                sp.connection.ackBlockChangesUpTo(sequence);
            }catch (Throwable ignored){
            }
        }
    }

    @Override
    public void tickEffects(Player player){
        Map<Long, Progress> map = blockDamage.get(player.getUniqueId());
        if(map == null || map.isEmpty()){
            return;
        }
        try {
            ServerPlayer sp = handle(player);
            ServerLevel level = sp.serverLevel();
            Iterator<Map.Entry<Long, Progress>> it = map.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Long, Progress> e = it.next();
                BlockPos pos = BlockPos.of(e.getKey());
                Progress p = e.getValue();
                if(level.getBlockState(pos).isAir() || p.delay <= 0){
                    level.destroyBlockProgress(sp.getId(), pos, -1);
                    broadcastBlockDamage(sp, pos, -1);
                    it.remove();
                }else{
                    p.delay--;
                }
            }
        }catch (Throwable t){
            if(!degradedTick){
                degradedTick = true;
                LOG.log(Level.WARNING, "block-damage tick failed; degrading.", t);
            }
        }
    }

    private Map<Long, Progress> mapFor(ServerPlayer sp){
        return blockDamage.computeIfAbsent(sp.getUUID(), k -> new HashMap<>());
    }

    private void clearProgress(ServerPlayer sp, ServerLevel level, BlockPos pos){
        Map<Long, Progress> map = blockDamage.get(sp.getUUID());
        if(map != null){
            map.remove(pos.asLong());
        }
        level.destroyBlockProgress(sp.getId(), pos, -1);
        broadcastBlockDamage(sp, pos, -1);
    }

    private void broadcastBlockDamage(ServerPlayer sp, BlockPos pos, int stage){
        if(plugin == null){
            return;
        }
        byte[] msg = VisorCodec.encode(new BlockDamageOut(
                sp.getUUID(),
                new VBlockPos(pos.getX(), pos.getY(), pos.getZ()),
                stage));
        Player self = sp.getBukkitEntity();
        self.sendPluginMessage(plugin, VisorProtocol.CHANNEL, msg);
        for(Player t : trackers(self)){
            if(!t.equals(self)){
                t.sendPluginMessage(plugin, VisorProtocol.CHANNEL, msg);
            }
        }
    }

    private float getDestroyProgress(BlockState state, ServerPlayer sp, ServerLevel level, BlockPos pos, ItemStack item){
        float speed = state.getDestroySpeed(level, pos);
        if(speed == -1.0F){
            return 0.0F;
        }
        int divisor = !state.requiresCorrectToolForDrops() || item.isCorrectToolForDrops(state) ? 30 : 100;
        return damageStep(sp, item, state) / speed / (float) divisor;
    }

    private float damageStep(ServerPlayer sp, ItemStack item, BlockState state){
        float f = item.getDestroySpeed(state);
        if(f > 1.0F){
            int eff = EnchantmentHelper.getBlockEfficiency(sp);
            if(eff > 0 && !item.isEmpty()){
                f += (float) (eff * eff + 1);
            }
        }
        if(MobEffectUtil.hasDigSpeed(sp)){
            f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(sp) + 1) * 0.2F;
        }
        if(sp.hasEffect(MobEffects.DIG_SLOWDOWN)){
            float g = switch(sp.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()){
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            f *= g;
        }
        if(sp.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(sp)){
            f /= 5.0F;
        }
        if(!sp.onGround()){
            f /= 5.0F;
        }
        return f;
    }

    @Override
    public Set<Player> trackers(Player player){
        Set<Player> out = new HashSet<>();
        double rangeSq = 256.0 * 256.0;
        for(Player other : player.getWorld().getPlayers()){
            if(!other.equals(player)
                    && other.getLocation().distanceSquared(player.getLocation()) <= rangeSq){
                out.add(other);
            }
        }
        return out;
    }

    private static final class Progress {
        long delay;
        float damage;
    }
}

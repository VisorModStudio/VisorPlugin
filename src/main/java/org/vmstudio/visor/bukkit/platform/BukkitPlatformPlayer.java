package org.vmstudio.visor.bukkit.platform;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.vmstudio.visor.common.platform.PlatformPlayer;
import org.vmstudio.visor.protocol.DirectionValue;
import org.vmstudio.visor.protocol.VisorProtocol;
import org.vmstudio.visor.protocol.value.VBlockPos;

public final class BukkitPlatformPlayer implements PlatformPlayer {
    private final Player handle;
    private final BukkitPlatformServer server;

    public BukkitPlatformPlayer(Player handle, BukkitPlatformServer server){
        this.handle = handle;
        this.server = server;
    }

    public Player handle(){
        return handle;
    }

    @Override
    public UUID uuid(){
        return handle.getUniqueId();
    }

    @Override
    public String name(){
        return handle.getName();
    }

    @Override
    public boolean isOp(){
        return handle.isOp();
    }

    @Override
    public boolean isOnline(){
        return handle.isOnline();
    }

    @Override
    public void sendPayload(byte[] channelData){
        if(handle.isOnline()){
            handle.sendPluginMessage(server.plugin(), VisorProtocol.CHANNEL, channelData);
        }
    }

    @Override
    public void disconnect(String reason){
        handle.kickPlayer(reason);
    }

    @Override
    public void teleportAbsolute(double x, double y, double z){
        Location loc = new Location(handle.getWorld(), x, y, z,
                handle.getLocation().getYaw(), handle.getLocation().getPitch());
        if(server.isFolia()){
            handle.teleportAsync(loc);
        }else{
            handle.teleport(loc);
        }
    }

    @Override
    public void resetFallDistance(){
        handle.setFallDistance(0f);
    }

    @Override
    public void setForcedCrawl(boolean crawling){
        server.adapter().forceCrawlPose(handle, crawling);
    }

    @Override
    public void swingAttack(int entityId, boolean shiftKeyDown, boolean mainHand){
        server.adapter().swingAttack(handle, entityId, shiftKeyDown, mainHand);
    }

    @Override
    public void blockSwing(VBlockPos pos, DirectionValue direction, boolean mainHand, int sequence, long repairDelayTicks){
        server.adapter().blockSwing(handle, pos, direction, mainHand, sequence, repairDelayTicks);
    }

    @Override
    public void tickEffects(){
        server.adapter().tickEffects(handle);
    }

    @Override
    public void swellNearbyCreepers(double distance, double hmdOffsetX, double hmdOffsetY, double hmdOffsetZ){
        if(!handle.isOnline()){
            return;
        }
        Location head = handle.getLocation().add(hmdOffsetX, hmdOffsetY, hmdOffsetZ);
        double rangeSq = distance * distance;
        double scan = distance + 2.0;
        for(Entity e : handle.getNearbyEntities(scan, scan, scan)){
            if(e instanceof Creeper creeper){
                creeper.setIgnited(creeper.getLocation().distanceSquared(head) <= rangeSq);
            }
        }
    }

    @Override
    public Set<PlatformPlayer> trackers(){
        return server.adapter().trackers(handle).stream()
                .map(server::wrap)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o){
        return o instanceof BukkitPlatformPlayer other && other.uuid().equals(uuid());
    }

    @Override
    public int hashCode(){
        return uuid().hashCode();
    }
}

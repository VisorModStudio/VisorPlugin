package org.vmstudio.visorplugin.bukkit.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vmstudio.visorplugin.common.platform.PlatformPlayer;
import org.vmstudio.visorplugin.api.network.buffer.DirectionValue;
import org.vmstudio.visorplugin.api.network.VisorNetwork;
import org.vmstudio.visorplugin.api.network.buffer.PoseDataBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseElementBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseHandBuffer;
import org.vmstudio.visorplugin.api.network.buffer.PoseHandJointBuffer;
import org.vmstudio.visorplugin.api.network.buffer.VBlockPos;
import org.vmstudio.visorplugin.api.player.VRHandJointType;

public final class BukkitPlatformPlayer implements PlatformPlayer {
    private static final String TRACKER_TAG = "visor_tracker_debug";
    private static final String HAND_TAG = "visor_hand_debug";

    private static final float TRACKER_SCALE = 0.12f;

    private static final float HAND_SCALE = 0.02f;

    private static final Material[] TRACKER_BLOCKS = {
            Material.GLOWSTONE, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK,
            Material.PURPLE_WOOL, Material.MAGENTA_WOOL,
            Material.RED_WOOL, Material.BLUE_WOOL,
            Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL, Material.CYAN_WOOL,
            Material.LIME_WOOL, Material.PINK_WOOL,
            Material.GREEN_WOOL, Material.BROWN_WOOL,
            Material.WHITE_WOOL, Material.BLACK_WOOL
    };


    private static final Material[] HAND_BLOCKS = createHandBlocks();

    private final Player handle;
    private final BukkitPlatformServer server;
    private Map<Integer, DebugStand> trackerStands;
    private Map<Integer, DebugStand> handStands;

    private static final class DebugStand {
        private final BlockDisplay display;
        private final double anchorX;
        private final double anchorY;
        private final double anchorZ;
        private double x;
        private double y;
        private double z;

        private DebugStand(BlockDisplay display, double x, double y, double z){
            this.display = display;
            this.anchorX = x;
            this.anchorY = y;
            this.anchorZ = z;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

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
    public double x(){
        return handle.getLocation().getX();
    }

    @Override
    public double y(){
        return handle.getLocation().getY();
    }

    @Override
    public double z(){
        return handle.getLocation().getZ();
    }

    @Override
    public void sendPayload(byte[] channelData){
        if(handle.isOnline()){
            handle.sendPluginMessage(server.plugin(), VisorNetwork.CHANNEL, channelData);
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
    public void showTrackerDebug(PoseDataBuffer pose){
        try {
            if(!handle.isOnline() || pose == null){
                clearTrackerDebug();
                return;
            }
            if(trackerStands == null){
                trackerStands = new HashMap<>();
            }
            PoseElementBuffer[] tr = pose.trackers().toArray();
            Vector3fc[] points = new Vector3fc[1 + tr.length];
            points[0] = pose.hmd().position();
            for(int i = 0; i < tr.length; i++){
                points[i + 1] = tr[i] == null ? null : tr[i].position();
            }
            renderDebug(trackerStands, pose, points, TRACKER_BLOCKS, TRACKER_SCALE, TRACKER_TAG);
        }catch (Throwable ignored){
        }
    }

    @Override
    public void showHandDebug(PoseDataBuffer pose){
        try {
            if(!handle.isOnline() || pose == null){
                clearHandDebug();
                return;
            }
            if(handStands == null){
                handStands = new HashMap<>();
            }
            int count = VRHandJointType.COUNT;
            Vector3fc[] points = new Vector3fc[count * 2];
            fillHandPoints(points, 0, pose.hands().leftHand());
            fillHandPoints(points, count, pose.hands().rightHand());
            renderDebug(handStands, pose, points, HAND_BLOCKS, HAND_SCALE, HAND_TAG);
        }catch (Throwable ignored){
        }
    }

    private static void fillHandPoints(Vector3fc[] out, int offset, PoseHandBuffer hand){
        for(PoseHandJointBuffer joint : hand.joints()){
            out[offset + joint.type().ordinal()] = joint.position();
        }
    }


    private void renderDebug(Map<Integer, DebugStand> stands,
                             PoseDataBuffer pose,
                             Vector3fc[] points,
                             Material[] blocks,
                             float scale,
                             String tag){
        double front = 1.5;
        double alpha = 0.35;

        PoseElementBuffer hmd = pose.hmd();

        Location pl = handle.getLocation();
        World world = pl.getWorld();
        Vector3fc h = hmd.position();

        Quaternionfc q = hmd.orientation();
        double hfX = -2.0 * (q.x() * q.z() + q.w() * q.y());
        double hfZ = -1.0 + 2.0 * (q.x() * q.x() + q.y() * q.y());
        double hfLen = Math.sqrt(hfX * hfX + hfZ * hfZ);
        double hmdFwdX = hfLen > 1.0e-4 ? hfX / hfLen : 0.0;
        double hmdFwdZ = hfLen > 1.0e-4 ? hfZ / hfLen : -1.0;
        double hmdRightX = -hmdFwdZ;
        double hmdRightZ = hmdFwdX;

        Vector facing = pl.getDirection();
        double fLen = Math.sqrt(facing.getX() * facing.getX() + facing.getZ() * facing.getZ());
        double fwdX = fLen > 1.0e-4 ? facing.getX() / fLen : 0.0;
        double fwdZ = fLen > 1.0e-4 ? facing.getZ() / fLen : 1.0;
        double rightX = -fwdZ;
        double rightZ = fwdX;
        double ax = pl.getX() + fwdX * front;
        double ay = pl.getY() + h.y();
        double az = pl.getZ() + fwdZ * front;

        for(int i = 0; i < points.length; i++){
            Vector3fc o = points[i];
            if(o == null){
                DebugStand gone = stands.remove(i);
                if(gone != null){
                    gone.display.remove();
                }
                continue;
            }
            double rawDx = o.x() - h.x();
            double dy = o.y() - h.y();
            double rawDz = o.z() - h.z();
            double dx = rawDx * hmdRightX + rawDz * hmdRightZ;
            double dz = -(rawDx * hmdFwdX + rawDz * hmdFwdZ);
            double mx = ax + rightX * dx - fwdX * dz;
            double my = ay + dy;
            double mz = az + rightZ * dx - fwdZ * dz;

            DebugStand stand = stands.get(i);
            if(stand == null || stand.display.isDead()){
                Material block = blocks[i];
                BlockDisplay display = world.spawn(new Location(world, mx, my, mz), BlockDisplay.class, d -> {
                    d.setBlock(block.createBlockData());
                    d.setBrightness(new Display.Brightness(15, 15));
                    d.setGravity(false);
                    d.setInvulnerable(true);
                    d.setPersistent(false);
                    d.setSilent(true);
                    d.setViewRange(256.0f);
                    d.addScoreboardTag(tag);
                    d.setTransformation(new Transformation(
                            new Vector3f(-scale / 2f, -scale / 2f, -scale / 2f),
                            new Quaternionf(),
                            new Vector3f(scale, scale, scale),
                            new Quaternionf()));
                });
                stands.put(i, new DebugStand(display, mx, my, mz));
            }else{
                stand.x += (mx - stand.x) * alpha;
                stand.y += (my - stand.y) * alpha;
                stand.z += (mz - stand.z) * alpha;
                BlockDisplay d = stand.display;
                d.setInterpolationDelay(0);
                d.setInterpolationDuration(2);
                d.setTransformation(new Transformation(
                        new Vector3f((float) (stand.x - stand.anchorX) - scale / 2f,
                                (float) (stand.y - stand.anchorY) - scale / 2f,
                                (float) (stand.z - stand.anchorZ) - scale / 2f),
                        new Quaternionf(),
                        new Vector3f(scale, scale, scale),
                        new Quaternionf()));
            }
        }
    }

    private static Material[] createHandBlocks(){
        int count = VRHandJointType.COUNT;
        Material[] out = new Material[count * 2];
        for(int i = 0; i < count; i++){
            VRHandJointType joint = VRHandJointType.fromIndex(i);
            out[i] = handBlock(joint, true);
            out[i + count] = handBlock(joint, false);
        }
        return out;
    }

    private static Material handBlock(VRHandJointType joint, boolean left){
        return switch(joint){
            case PALM ->
                    left ? Material.WHITE_WOOL : Material.WHITE_CONCRETE;
            case WRIST ->
                    left ? Material.GRAY_WOOL : Material.GRAY_CONCRETE;
            case THUMB_METACARPAL, THUMB_PROXIMAL, THUMB_DISTAL, THUMB_TIP ->
                    left ? Material.RED_WOOL : Material.RED_CONCRETE;
            case INDEX_METACARPAL, INDEX_PROXIMAL, INDEX_INTERMEDIATE, INDEX_DISTAL, INDEX_TIP ->
                    left ? Material.ORANGE_WOOL : Material.ORANGE_CONCRETE;
            case MIDDLE_METACARPAL, MIDDLE_PROXIMAL, MIDDLE_INTERMEDIATE, MIDDLE_DISTAL, MIDDLE_TIP ->
                    left ? Material.YELLOW_WOOL : Material.YELLOW_CONCRETE;
            case RING_METACARPAL, RING_PROXIMAL, RING_INTERMEDIATE, RING_DISTAL, RING_TIP ->
                    left ? Material.LIME_WOOL : Material.LIME_CONCRETE;
            case LITTLE_METACARPAL, LITTLE_PROXIMAL, LITTLE_INTERMEDIATE, LITTLE_DISTAL, LITTLE_TIP ->
                    left ? Material.LIGHT_BLUE_WOOL : Material.LIGHT_BLUE_CONCRETE;
        };
    }

    private static void removeAllStands(Map<Integer, DebugStand> stands){
        if(stands == null){
            return;
        }
        for(DebugStand stand : stands.values()){
            try {
                stand.display.remove();
            }catch (Throwable ignored){
            }
        }
        stands.clear();
    }

    @Override
    public void clearTrackerDebug(){
        removeAllStands(trackerStands);
    }

    @Override
    public void clearHandDebug(){
        removeAllStands(handStands);
    }

    public static void removeTrackerDebug(Server server){
        removeTagged(server, TRACKER_TAG);
    }

    public static void removeHandDebug(Server server){
        removeTagged(server, HAND_TAG);
    }

    public static void removeAllDebug(Server server){
        removeTagged(server, TRACKER_TAG);
        removeTagged(server, HAND_TAG);
    }

    private static void removeTagged(Server server, String tag){
        for(World world : server.getWorlds()){
            try {
                for(Entity e : world.getEntities()){
                    if(e.getScoreboardTags().contains(tag)){
                        e.remove();
                    }
                }
            }catch (Throwable ignored){
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

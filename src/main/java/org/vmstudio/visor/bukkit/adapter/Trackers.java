package org.vmstudio.visor.bukkit.adapter;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

final class Trackers {
    private Trackers(){}

    private static volatile boolean resolved;
    private static volatile Method trackedByMethod;

    static Set<Player> of(Player player, double fallbackRangeBlocks){
        Set<Player> reflected = viaReflection(player);
        if(reflected != null){
            return reflected;
        }
        return byDistance(player, fallbackRangeBlocks);
    }

    @SuppressWarnings("unchecked")
    private static Set<Player> viaReflection(Player player){
        if(!resolved){
            synchronized(Trackers.class){
                if(!resolved){
                    trackedByMethod = resolve(player);
                    resolved = true;
                }
            }
        }
        Method m = trackedByMethod;
        if(m == null){
            return null;
        }
        try {
            Object result = m.invoke(player);
            if(result instanceof Collection<?> c){
                Set<Player> out = new HashSet<>();
                for(Object o : c){
                    if(o instanceof Player p){
                        out.add(p);
                    }
                }
                return out;
            }
        }catch (Throwable ignored){}
        return null;
    }

    private static Method resolve(Player player){
        for(String name : new String[]{"getTrackedBy", "getTrackedPlayers"}){
            try {
                Method m = player.getClass().getMethod(name);
                m.setAccessible(true);
                return m;
            }catch (Throwable ignored){
            }
        }
        return null;
    }

    private static Set<Player> byDistance(Player player, double range){
        Set<Player> out = new HashSet<>();
        double rangeSq = range * range;
        for(Player other : player.getWorld().getPlayers()){
            if(other.equals(player)){
                continue;
            }
            if(other.getLocation().distanceSquared(player.getLocation()) <= rangeSq){
                out.add(other);
            }
        }
        return out;
    }
}

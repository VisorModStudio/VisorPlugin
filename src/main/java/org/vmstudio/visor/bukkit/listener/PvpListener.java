package org.vmstudio.visor.bukkit.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.vmstudio.visor.common.VisorServer;
import org.vmstudio.visor.common.session.VisorSession;
import org.vmstudio.visor.common.settings.VisorSettings;

public final class PvpListener implements Listener {
    private final VisorServer visor;

    public PvpListener(VisorServer visor){
        this.visor = visor;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player victim)){
            return;
        }
        Player attacker = resolveAttacker(event);
        if(attacker == null || attacker.equals(victim)){
            return;
        }

        boolean attackerVr = isVr(attacker);
        boolean victimVr = isVr(victim);
        if(!attackerVr && !victimVr){
            return;
        }

        VisorSettings s = visor.settings();
        boolean allowed = (attackerVr && victimVr) ? s.pvpVRvsVR() : s.pvpVRvsVanilla();
        if(allowed){
            return;
        }

        event.setCancelled(true);
        if(s.notifyPvpBlocked()){
            attacker.sendMessage("§cPvP involving VR players is disabled here");
        }
    }

    private Player resolveAttacker(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player direct){
            return direct;
        }
        if(event.getDamager() instanceof Projectile projectile
                && projectile.getShooter() instanceof Player shooter){
            return shooter;
        }
        return null;
    }

    private boolean isVr(Player player){
        VisorSession session = visor.sessions().get(player.getUniqueId());
        return session != null && session.vrActive();
    }
}

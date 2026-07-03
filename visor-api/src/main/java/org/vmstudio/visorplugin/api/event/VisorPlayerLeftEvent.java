package org.vmstudio.visorplugin.api.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.player.VisorPlayer;

/**
 * Fired when a player with the Visor mod disconnects, VR or not - see {@link #isVRPlayer()}
 */
public final class VisorPlayerLeftEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final VisorPlayer visorPlayer;

    public VisorPlayerLeftEvent(VisorPlayer vrPlayer){
        this.visorPlayer = vrPlayer;
    }


    public boolean isVRPlayer(){
        return this.visorPlayer.isVR();
    }

    @Override
    public @NotNull HandlerList getHandlers(){
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}

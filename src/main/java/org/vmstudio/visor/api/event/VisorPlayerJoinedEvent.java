package org.vmstudio.visor.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.player.VisorPlayer;

/**
 * Fired when a player with the Visor mod joins (after the handshake), VR or not - see {@link #isVRPlayer()}
 */
public final class VisorPlayerJoinedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final VisorPlayer visorPlayer;

    public VisorPlayerJoinedEvent(VisorPlayer visorPlayer){
        this.visorPlayer = visorPlayer;
    }

    public VisorPlayer getVisorPlayer(){
        return visorPlayer;
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

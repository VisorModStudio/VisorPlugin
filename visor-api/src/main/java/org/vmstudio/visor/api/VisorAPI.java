package org.vmstudio.visor.api;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.network.VisorChannel;
import org.vmstudio.visor.api.network.VisorChannelHandler;
import org.vmstudio.visor.api.player.VRPlayer;
import org.vmstudio.visor.api.player.VisorPlayer;
import org.vmstudio.visor.api.nms.McVersion;

/**
 * Central access point for all Visor Plugin API functionality
 */
public interface VisorAPI {
    /**
     * Get Visor API
     *
     * @return Visor API
     */
    static VisorAPI get(){
        if(Instance.api == null){
            throw new IllegalStateException("VisorAPI is not available. Add [VisorPlugin] to depends in plugin.yml");
        }
        return Instance.api;
    }

    /**
     * Visor core network/protocol version
     *
     * @return Network version from {@link VisorProtocol}
     */
    static int networkVersion(){
        return VisorProtocol.CORE_NETWORK_VERSION;
    }

    /**
     * Minecraft version this server runs
     *
     * @return Minecraft version
     */
    McVersion minecraftVersion();

    /**
     * If there is a VR player with specified uuid
     *
     * @return true/false
     */
    boolean isVRPlayer(UUID uuid);

    /**
     * If specified player has VR
     *
     * @return true/false
     */
    boolean isVRPlayer(Player player);

    /**
     * Get Visor player with specified uuid.
     *
     * @return Visor player or null if not found
     */
    @Nullable
    VisorPlayer getVisorPlayer(UUID uuid);

    /**
     * Get Visor player from MC player.
     *
     * @return Visor player or null if not found
     */
    @Nullable
    VisorPlayer getVisorPlayer(Player player);

    /**
     * Get all Visor players on server.
     * @return Visor players
     */
    Collection<VisorPlayer> getVisorPlayers();

    /**
     * Get player in VR with specified uuid.
     *
     * @return VR player or null if not found
     */
    @Nullable
    VRPlayer getVRPlayer(UUID uuid);

    /**
     * Get player in VR from MC player.
     *
     * @return VR player or null if not found
     */
    @Nullable
    VRPlayer getVRPlayer(Player player);

    /**
     * Get Visor player from MC player.
     *
     * @return Visor player or null if not found
     */
    Collection<VRPlayer> getVRPlayers();

    /**
     * Register a custom channel for payloads with the matching Visor addon.
     * Use this overload when you only send and receive typed
     * packets via {@link VisorChannel#on} and {@link VisorChannel#send}
     *
     * @param owner the plugin that registering channel
     * @param channel channel name in {@code namespace:path} form (like {@code "myaddon:hello"})
     * @return registered channel
     */
    VisorChannel registerChannel(Plugin owner, String channel);

    /**
     * Register a custom channel with a raw handler.
     * This is like {@link #registerChannel(Plugin, String)} but any inbound
     * message whose first byte does not match a typed packet registered with
     * {@link VisorChannel#on} is sending to {@code handler} with raw bytes
     *
     * @param owner the plugin that registering channel
     * @param channel channel name in {@code namespace:path} form (like {@code "myaddon:hello"})
     * @param handler fallback for payloads the typed packet layer does not handle
     * @return registered channel
     */
    VisorChannel registerChannel(Plugin owner, String channel, VisorChannelHandler handler);

    @ApiStatus.Internal
    final class Instance {
        private static VisorAPI api;

        private Instance(){}

        @ApiStatus.Internal
        public static void set(VisorAPI api){
            Instance.api = api;
        }
    }
}

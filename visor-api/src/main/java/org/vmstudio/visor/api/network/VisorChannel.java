package org.vmstudio.visor.api.network;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.entity.Player;

public interface VisorChannel {
    /**
     * @return channel name in {@code namespace:path} form
     */
    String name();

    /**
     * Registers a typed inbound packet
     *
     * @param id packet discriminator, 0..255, matching {@link VisorPacket#id()}
     * @param reader decodes the payload (id byte already consumed) into a packet
     * @param handler receives the sending player and the decoded packet
     * @return this channel, for chaining
     * @throws IllegalArgumentException if {@code id} is outside 0..255
     */
    <T extends VisorPacket> VisorChannel on(int id, Function<VisorBuf, T> reader, BiConsumer<Player, T> handler);

    /**
     * Encodes and sends a typed packet to MC player
     */
    void send(Player player, VisorPacket packet);

    /**
     * Encodes and sends a typed packet to a player by uuid
     *
     * @param uuid player's uuid
     * @param packet packet to send
     */
    void send(UUID uuid, VisorPacket packet);

    /**
     * Sends a raw payload
     */
    void send(Player player, byte[] payload);

    /**
     * Sends a raw payload to a player by uuid
     */
    void send(UUID uuid, byte[] payload);

    /**
     * Unregister this channel and its listeners
     */
    void unregister();
}

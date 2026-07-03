package org.vmstudio.visorplugin.api.network;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.entity.Player;
import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;

public interface VisorChannel {

    ChannelID channelId();


    <T extends VisorPayload> VisorChannel on(int id, Function<VisorByteBuf, T> reader, BiConsumer<Player, T> handler);


    void send(Player player, VisorPayload packet);

    void send(UUID uuid, VisorPayload packet);

    void send(Player player, byte[] payload);

    void send(UUID uuid, byte[] payload);


    void unregister();
}

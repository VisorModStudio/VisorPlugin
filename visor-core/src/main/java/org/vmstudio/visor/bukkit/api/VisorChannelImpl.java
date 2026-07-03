package org.vmstudio.visor.bukkit.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.Nullable;
import org.vmstudio.visor.api.network.ChannelID;
import org.vmstudio.visor.api.network.VisorChannel;
import org.vmstudio.visor.api.network.VisorChannelHandler;
import org.vmstudio.visor.api.network.VisorPayload;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;

public final class VisorChannelImpl implements VisorChannel {
    private final Plugin owner;
    private final ChannelID channelId;
    @Nullable
    private final VisorChannelHandler rawHandler;
    private final Map<Integer, Entry> registry = new HashMap<>();
    private PluginMessageListener listener;

    public VisorChannelImpl(Plugin owner, ChannelID channelId, @Nullable VisorChannelHandler rawHandler){
        this.owner = owner;
        this.channelId = channelId;
        this.rawHandler = rawHandler;
    }

    public void attachListener(PluginMessageListener listener){
        this.listener = listener;
    }

    public void handleIncoming(Player player, byte[] message){
        if(message != null && message.length > 0){
            VisorByteBuf buf = VisorByteBuf.wrap(message);
            int id = buf.readUnsignedByte();
            Entry entry = registry.get(id);
            if(entry != null){
                dispatch(entry, player, buf);
                return;
            }
        }
        if(rawHandler != null){
            rawHandler.onMessage(player, message);
        }
    }

    @SuppressWarnings("unchecked")
    private void dispatch(Entry entry, Player player, VisorByteBuf buf){
        VisorPayload packet = ((Function<VisorByteBuf, VisorPayload>) entry.reader).apply(buf);
        ((BiConsumer<Player, VisorPayload>) entry.handler).accept(player, packet);
    }

    @Override
    public ChannelID channelId(){
        return channelId;
    }

    @Override
    public <T extends VisorPayload> VisorChannel on(int id, Function<VisorByteBuf, T> reader, BiConsumer<Player, T> handler){
        if(id < 0 || id > 0xFF){
            throw new IllegalArgumentException("Packet id must be 0..255, got " + id);
        }
        registry.put(id, new Entry(reader, handler));
        return this;
    }

    @Override
    public void send(Player player, VisorPayload packet){
        VisorByteBuf buf = VisorByteBuf.create();
        packet.write(buf);
        send(player, buf.toBytes());
    }

    @Override
    public void send(UUID uuid, VisorPayload packet){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null){
            send(player, packet);
        }
    }

    @Override
    public void send(Player player, byte[] payload){
        if(player.isOnline()){
            player.sendPluginMessage(owner, channelId.asString(), payload);
        }
    }

    @Override
    public void send(UUID uuid, byte[] payload){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null){
            send(player, payload);
        }
    }

    @Override
    public void unregister(){
        Messenger messenger = Bukkit.getMessenger();
        if(listener != null){
            messenger.unregisterIncomingPluginChannel(owner, channelId.asString(), listener);
        }
        messenger.unregisterOutgoingPluginChannel(owner, channelId.asString());
    }

    private record Entry(Function<VisorByteBuf, ? extends VisorPayload> reader,
                         BiConsumer<Player, ? extends VisorPayload> handler) {
    }
}

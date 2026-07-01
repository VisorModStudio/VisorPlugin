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
import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.api.network.VisorChannel;
import org.vmstudio.visor.api.network.VisorChannelHandler;
import org.vmstudio.visor.api.network.VisorPacket;

public final class VisorChannelImpl implements VisorChannel {
    private final Plugin owner;
    private final String channel;
    @Nullable
    private final VisorChannelHandler rawHandler;
    private final Map<Integer, Entry> registry = new HashMap<>();
    private PluginMessageListener listener;

    public VisorChannelImpl(Plugin owner, String channel, @Nullable VisorChannelHandler rawHandler){
        this.owner = owner;
        this.channel = channel;
        this.rawHandler = rawHandler;
    }

    public void attachListener(PluginMessageListener listener){
        this.listener = listener;
    }

    public void handleIncoming(Player player, byte[] message){
        if(message != null && message.length > 0){
            VisorBuf buf = VisorBuf.wrap(message);
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
    private void dispatch(Entry entry, Player player, VisorBuf buf){
        VisorPacket packet = ((Function<VisorBuf, VisorPacket>) entry.reader).apply(buf);
        ((BiConsumer<Player, VisorPacket>) entry.handler).accept(player, packet);
    }

    @Override
    public String name(){
        return channel;
    }

    @Override
    public <T extends VisorPacket> VisorChannel on(int id, Function<VisorBuf, T> reader, BiConsumer<Player, T> handler){
        if(id < 0 || id > 0xFF){
            throw new IllegalArgumentException("Packet id must be 0..255, got " + id);
        }
        registry.put(id, new Entry(reader, handler));
        return this;
    }

    @Override
    public void send(Player player, VisorPacket packet){
        VisorBuf buf = VisorBuf.create();
        buf.writeByte(packet.id());
        packet.write(buf);
        send(player, buf.toBytes());
    }

    @Override
    public void send(UUID uuid, VisorPacket packet){
        Player player = Bukkit.getPlayer(uuid);
        if(player != null){
            send(player, packet);
        }
    }

    @Override
    public void send(Player player, byte[] payload){
        if(player.isOnline()){
            player.sendPluginMessage(owner, channel, payload);
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
            messenger.unregisterIncomingPluginChannel(owner, channel, listener);
        }
        messenger.unregisterOutgoingPluginChannel(owner, channel);
    }

    private record Entry(Function<VisorBuf, ? extends VisorPacket> reader,
                         BiConsumer<Player, ? extends VisorPacket> handler) {
    }
}

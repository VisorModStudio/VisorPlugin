package org.vmstudio.visorplugin.api.network;

import org.vmstudio.visorplugin.api.network.buffer.VisorByteBuf;

/**
 * Payload for Visor related network packets
 */
public interface VisorPayload {


    default void write(VisorByteBuf buffer) {
        buffer.writeByte(payloadId());
        onWrite(buffer);
    }

    void onWrite(VisorByteBuf buffer);


    byte payloadId();
}

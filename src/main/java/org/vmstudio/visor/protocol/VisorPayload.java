package org.vmstudio.visor.protocol;

public interface VisorPayload {

    VisorPayloadId id();

    void write(VisorByteBuf buf);
}

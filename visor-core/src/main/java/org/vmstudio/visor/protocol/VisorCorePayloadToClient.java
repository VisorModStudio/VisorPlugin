package org.vmstudio.visor.protocol;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.VisorPayloadToClient;

public interface VisorCorePayloadToClient extends VisorPayloadToClient {

    @NotNull
    VisorCorePayloadId payloadType();

    @Override
    default byte payloadId() {
        return payloadType().byteOrdinal();
    }
}

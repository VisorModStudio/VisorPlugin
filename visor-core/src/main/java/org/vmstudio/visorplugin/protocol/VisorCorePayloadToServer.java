package org.vmstudio.visorplugin.protocol;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visorplugin.api.network.VisorPayloadToServer;

public interface VisorCorePayloadToServer extends VisorPayloadToServer {

    @NotNull
    VisorCorePayloadId payloadType();

    @Override
    default byte payloadId() {
        return payloadType().byteOrdinal();
    }
}

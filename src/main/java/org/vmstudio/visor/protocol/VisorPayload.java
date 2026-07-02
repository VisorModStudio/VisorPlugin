package org.vmstudio.visor.protocol;

import org.vmstudio.visor.api.network.VisorBuf;

public interface VisorPayload {

    VisorPayloadId id();

    void write(VisorBuf buf);
}

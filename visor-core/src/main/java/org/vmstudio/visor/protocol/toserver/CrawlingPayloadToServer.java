package org.vmstudio.visor.protocol.toserver;

import org.jetbrains.annotations.NotNull;
import org.vmstudio.visor.api.network.buffer.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorCorePayloadToServer;
import org.vmstudio.visor.protocol.VisorCorePayloadId;

public record CrawlingPayloadToServer(boolean crawling) implements VisorCorePayloadToServer {
    @Override
    public @NotNull VisorCorePayloadId payloadType(){
        return VisorCorePayloadId.CRAWLING;
    }

    @Override
    public void onWrite(VisorByteBuf buf){
        buf.writeBoolean(crawling);
    }

    public static CrawlingPayloadToServer read(VisorByteBuf buf){
        return new CrawlingPayloadToServer(buf.readBoolean());
    }
}

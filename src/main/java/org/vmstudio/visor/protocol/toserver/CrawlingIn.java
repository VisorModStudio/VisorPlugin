package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.api.network.VisorBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record CrawlingIn(boolean crawling) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.CRAWLING;
    }

    @Override
    public void write(VisorBuf buf){
        buf.writeBoolean(crawling);
    }

    public static CrawlingIn read(VisorBuf buf){
        return new CrawlingIn(buf.readBoolean());
    }
}

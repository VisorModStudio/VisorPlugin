package org.vmstudio.visor.protocol.toserver;

import org.vmstudio.visor.protocol.VisorByteBuf;
import org.vmstudio.visor.protocol.VisorInbound;
import org.vmstudio.visor.protocol.VisorPayloadId;

public record CrawlingIn(boolean crawling) implements VisorInbound {
    @Override
    public VisorPayloadId id(){
        return VisorPayloadId.CRAWLING;
    }

    @Override
    public void write(VisorByteBuf buf){
        buf.writeBoolean(crawling);
    }

    public static CrawlingIn read(VisorByteBuf buf){
        return new CrawlingIn(buf.readBoolean());
    }
}

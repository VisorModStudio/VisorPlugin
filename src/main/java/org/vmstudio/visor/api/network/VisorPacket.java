package org.vmstudio.visor.api.network;

public interface VisorPacket {
    int id();
    void write(VisorBuf buf);
}

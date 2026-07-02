package org.vmstudio.visor.protocol.value;

import org.vmstudio.visor.api.network.VisorBuf;

public record PoseElement(Vec3f position, Quat orientation){
    public void write(VisorBuf buf){
        buf.writeFloat(position.x()).writeFloat(position.y()).writeFloat(position.z());
        buf.writeFloat(orientation.x()).writeFloat(orientation.y()).writeFloat(orientation.z()).writeFloat(orientation.w());
    }

    public static PoseElement read(VisorBuf buf){
        Vec3f pos = new Vec3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
        Quat rot = new Quat(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
        return new PoseElement(pos, rot);
    }
}

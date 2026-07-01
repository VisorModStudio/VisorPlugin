package org.vmstudio.visor.bukkit.api;

import org.bukkit.util.Vector;
import org.vmstudio.visor.protocol.value.Quat;

public final class PoseMath {
    private PoseMath(){}

    /**
     * rotate vector {@code (vx, vy, vz)} by the quaternion {@code q}
     *
     * @param q the rotation quaternion
     * @param vx vector X
     * @param vy vector Y
     * @param vz vector Z
     * @return rotated vector
     */
    public static Vector rotate(Quat q, double vx, double vy, double vz){
        double tx = 2.0 * (q.y() * vz - q.z() * vy);
        double ty = 2.0 * (q.z() * vx - q.x() * vz);
        double tz = 2.0 * (q.x() * vy - q.y() * vx);
        double rx = vx + q.w() * tx + (q.y() * tz - q.z() * ty);
        double ry = vy + q.w() * ty + (q.z() * tx - q.x() * tz);
        double rz = vz + q.w() * tz + (q.x() * ty - q.y() * tx);
        return new Vector(rx, ry, rz);
    }

    /**
     * rotate a vector around the Y axis (matches joml {@code Vector3f.rotateY})
     *
     * @param v the vector to rotate
     * @param angle the angle in radians
     * @return rotated vector
     */
    public static Vector rotateY(Vector v, double angle){
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        return new Vector(v.getX() * c + v.getZ() * s, v.getY(), -v.getX() * s + v.getZ() * c);
    }
}

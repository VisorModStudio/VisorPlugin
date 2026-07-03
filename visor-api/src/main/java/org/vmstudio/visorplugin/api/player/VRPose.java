package org.vmstudio.visorplugin.api.player;

import org.bukkit.util.Vector;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Represents a VR spatial data (controller, hmd, eyes, body parts etc.)
 */
public final class VRPose {
    /**
     * An empty element with zero position, zero direction,
     * and identity rotation.
     */
    public static final VRPose EMPTY = new VRPose(new Vector3f(0, 0, 0), new Quaternionf());

    private final Vector3fc position;
    private final Vector3fc direction;
    private final Matrix4fc rotation;
    private final Matrix4fc invertedRotation;
    private final float yaw;
    private final float pitch;
    private final float roll;

    public VRPose(Vector3fc position, Quaternionfc rotation){
        this.position = position;
        this.rotation = new Matrix4f().rotation(rotation);
        this.invertedRotation = this.rotation.invert(new Matrix4f());
        this.direction = rotation.transform(new Vector3f(0, 0, -1));
        float[] a = extractAngles(rotation, this.direction);
        this.yaw = a[0];
        this.pitch = a[1];
        this.roll = a[2];
    }

    /**
     *
     * @return position
     */
    public Vector3fc getPosition(){
        return position;
    }

    /**
     *
     * @return position as a bukkit vector
     */
    public Vector getPositionMc(){
        return toMc(position);
    }

    /**
     *
     * @return direction of the element
     */
    public Vector3fc getDirection(){
        return direction;
    }

    /**
     *
     * @return direction as a bukkit vector
     */
    public Vector getDirectionMc(){
        return toMc(direction);
    }

    /**
     *
     * @return rotation matrix
     */
    public Matrix4fc getRotation(){
        return rotation;
    }

    /**
     *
     * @return inverted rotation matrix
     */
    public Matrix4fc getInvertedRotation(){
        return invertedRotation;
    }

    /**
     * Get custom vector
     * @return vector
     */
    public Vector3f getCustomVector(Vector3fc vec){
        return rotation.transformDirection(vec.x(), vec.y(), vec.z(), new Vector3f());
    }

    /**
     * Get custom vector
     * @return vector as a bukkit vector
     */
    public Vector getCustomVectorMc(Vector vec){
        return toMc(getCustomVector(fromMc(vec)));
    }

    /**
     * Reverse {@link VRPose#getCustomVector(Vector3fc)}
     * @param customVec vec
     * @return original vector
     */
    public Vector3f reverseCustomVector(Vector3fc customVec){
        return invertedRotation.transformDirection(customVec.x(), customVec.y(), customVec.z(), new Vector3f());
    }

    /**
     * Reverse {@link VRPose#getCustomVector(Vector3fc)}
     * @param customVec vec
     * @return original vector as a bukkit vector
     */
    public Vector reverseCustomVectorMc(Vector customVec){
        return toMc(reverseCustomVector(fromMc(customVec)));
    }

    /**
     * Get yaw in radians
     *
     * @return yaw
     */
    public float getYaw(){
        return yaw;
    }

    /**
     * Get yaw in degrees
     *
     * @return yaw in degrees
     */
    public float getYawDegrees(){
        return (float) Math.toDegrees(yaw);
    }

    /**
     * Get pitch in radians
     *
     * @return pitch
     */
    public float getPitch(){
        return pitch;
    }

    /**
     * Get pitch in degrees
     *
     * @return yaw in degrees
     */
    public float getPitchDegrees(){
        return (float) Math.toDegrees(pitch);
    }

    /**
     * Get roll in radians
     *
     * @return roll
     */
    public float getRoll(){
        return roll;
    }

    /**
     * Get roll in degrees
     *
     * @return roll in degrees
     */
    public float getRollDegrees(){
        return (float) Math.toDegrees(roll);
    }

    private static Vector toMc(Vector3fc v){
        return new Vector(v.x(), v.y(), v.z());
    }

    private static Vector3f fromMc(Vector v){
        return new Vector3f((float) v.getX(), (float) v.getY(), (float) v.getZ());
    }

    /**
     * inspired from Visor mod {@code VRPoseImpl.extractAngles}
     */
    private static float[] extractAngles(Quaternionfc quat, Vector3fc dir){
        double x = quat.x(), y = quat.y(), z = quat.z(), w = quat.w();
        if(w < 0){
            x = -x; y = -y; z = -z; w = -w;
        }

        double dx = dir.x(), dy = dir.y(), dz = dir.z();
        double dirLen = Math.sqrt(dx * dx + dy * dy + dz * dz);

        float pitch = dirLen > 1e-6
                ? (float) Math.asin(clamp(dy / dirLen, -1.0, 1.0))
                : 0f;

        double twistMagSq = z * z + w * w;
        float roll;
        if(twistMagSq > 1e-8){
            double r = -2.0 * Math.atan2(z, w);
            if(r > Math.PI) r -= 2 * Math.PI;
            if(r < -Math.PI) r += 2 * Math.PI;
            roll = (float) r;
        }else{
            roll = 0f;
        }

        float yaw;
        double horizLenSq = dx * dx + dz * dz;
        if(horizLenSq > 0.05){
            yaw = (float) Math.atan2(-dx, dz);
        }else if(twistMagSq > 1e-8){
            double n = Math.sqrt(twistMagSq);
            double bz = -z / n, bw = w / n;
            float sx = (float) (x * bw + y * bz);
            float sy = (float) (-x * bz + y * bw);
            float sz = (float) (w * bz + z * bw);
            float sw = (float) (w * bw - z * bz);
            Vector3f up = new Quaternionf(sx, sy, sz, sw).transform(new Vector3f(0, 1, 0));
            yaw = dy >= 0
                    ? (float) Math.atan2(up.x, -up.z)
                    : (float) Math.atan2(-up.x, up.z);
        } else{
            yaw = 0f;
        }

        return new float[]{yaw, pitch, roll};
    }

    private static double clamp(double v, double min, double max){
        return v < min ? min : Math.min(v, max);
    }
}

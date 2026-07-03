package org.vmstudio.visor.bukkit.api;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.vmstudio.visor.api.player.VRPlayerPose;
import org.vmstudio.visor.api.player.VRPoseHistory;
import org.vmstudio.visor.api.player.VRBodyPartType;
import org.vmstudio.visor.api.player.VRPose;

public class PoseHistoryImpl implements VRPoseHistory {
    private final List<VRPlayerPose> history;

    public PoseHistoryImpl(List<VRPlayerPose> history){
        this.history = history;
    }

    @Override
    public List<VRPlayerPose> getAllHistory(){
        return List.copyOf(history);
    }

    @Override
    public VRPlayerPose getEntry(int ticksBack){
        return history.get(ticksBack);
    }

    @Override
    public int getHistorySize(){
        return history.size();
    }

    @Override
    public Vector3f netMovement(VRBodyPartType bodyPart, int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return new Vector3f();
        }
        int back = clamp(maxTicksBack);
        Vector3fc last = position(history.get(0), bodyPart);
        Vector3fc old = position(history.get(back), bodyPart);
        if(last == null || old == null){
            return new Vector3f();
        }
        return new Vector3f(last).sub(old);
    }

    @Override
    public Vector3f headPivotNetMovement(int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return new Vector3f();
        }
        int back = clamp(maxTicksBack);
        return new Vector3f(history.get(0).getHeadPivot()).sub(history.get(back).getHeadPivot());
    }

    @Override
    public double averageSpeed(VRBodyPartType bodyPart, int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return 0;
        }
        int back = clamp(maxTicksBack);
        double sum = 0;
        int n = 0;
        for(int i = 0; i < back; i++){
            Vector3fc newer = position(history.get(i), bodyPart);
            Vector3fc older = position(history.get(i + 1), bodyPart);
            if(newer == null || older == null){
                continue;
            }
            sum += newer.distance(older);
            n++;
        }
        return n == 0 ? 0 : sum / n;
    }

    @Override
    public double headPivotAverageSpeed(int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return 0;
        }
        int back = clamp(maxTicksBack);
        double sum = 0;
        int n = 0;
        for(int i = 0; i < back; i++){
            sum += history.get(i).getHeadPivot().distance(history.get(i + 1).getHeadPivot());
            n++;
        }
        return n == 0 ? 0 : sum / n;
    }

    @Override
    public @Nullable Vector3f averagePosition(VRBodyPartType bodyPart, int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.isEmpty()){
            return null;
        }
        int back = clamp(maxTicksBack);
        float sx = 0;
        float sy = 0;
        float sz = 0;
        int n = 0;
        int count = 0;
        for(VRPlayerPose pose : history){
            Vector3fc p = position(pose, bodyPart);
            if(p != null){
                sx += p.x();
                sy += p.y();
                sz += p.z();
                n++;
            }
            if(++count >= back){
                break;
            }
        }
        return n == 0 ? null : new Vector3f(sx / n, sy / n, sz / n);
    }

    @Override
    public @Nullable Vector3f headPivotAveragePosition(int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.isEmpty()){
            return null;
        }
        int back = clamp(maxTicksBack);
        float sx = 0;
        float sy = 0;
        float sz = 0;
        int n = 0;
        int count = 0;
        for(VRPlayerPose pose : history){
            Vector3fc p = pose.getHeadPivot();
            sx += p.x();
            sy += p.y();
            sz += p.z();
            n++;
            if(++count >= back){
                break;
            }
        }
        return n == 0 ? null : new Vector3f(sx / n, sy / n, sz / n);
    }

    private void checkTicksBack(int ticksBack){
        if(ticksBack < 0 || ticksBack > HISTORY_LIMIT){
            throw new IllegalArgumentException("Value must be between 0 and " + HISTORY_LIMIT);
        }
    }

    private int clamp(int maxTicksBack){
        return Math.max(0, Math.min(maxTicksBack, history.size() - 1));
    }

    @Nullable
    private static Vector3fc position(VRPlayerPose pose, VRBodyPartType bodyPart){
        VRPose vp = pose.getPose(bodyPart);
        return vp == null ? null : vp.getPosition();
    }
}

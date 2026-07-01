package org.vmstudio.visor.bukkit.api;

import java.util.List;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;
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
    public Vector netMovement(VRBodyPartType bodyPart, int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return new Vector();
        }
        int back = clamp(maxTicksBack);
        Vector last = position(history.get(0), bodyPart);
        Vector old = position(history.get(back), bodyPart);
        if(last == null || old == null){
            return new Vector();
        }
        return last.clone().subtract(old);
    }

    @Override
    public Vector headPivotNetMovement(int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.size() <= 1){
            return new Vector();
        }
        int back = clamp(maxTicksBack);
        return history.get(0).getHeadPivot().subtract(history.get(back).getHeadPivot());
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
            Vector newer = position(history.get(i), bodyPart);
            Vector older = position(history.get(i + 1), bodyPart);
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
    public @Nullable Vector averagePosition(VRBodyPartType bodyPart, int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.isEmpty()){
            return null;
        }
        int back = clamp(maxTicksBack);
        double sx = 0;
        double sy = 0;
        double sz = 0;
        int n = 0;
        int count = 0;
        for(VRPlayerPose pose : history){
            Vector p = position(pose, bodyPart);
            if(p != null){
                sx += p.getX();
                sy += p.getY();
                sz += p.getZ();
                n++;
            }
            if(++count >= back){
                break;
            }
        }
        return n == 0 ? null : new Vector(sx / n, sy / n, sz / n);
    }

    @Override
    public @Nullable Vector headPivotAveragePosition(int maxTicksBack){
        checkTicksBack(maxTicksBack);
        if(history.isEmpty()){
            return null;
        }
        int back = clamp(maxTicksBack);
        double sx = 0;
        double sy = 0;
        double sz = 0;
        int n = 0;
        int count = 0;
        for(VRPlayerPose pose : history){
            Vector p = pose.getHeadPivot();
            sx += p.getX();
            sy += p.getY();
            sz += p.getZ();
            n++;
            if(++count >= back){
                break;
            }
        }
        return n == 0 ? null : new Vector(sx / n, sy / n, sz / n);
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
    private static Vector position(VRPlayerPose pose, VRBodyPartType bodyPart){
        VRPose vp = pose.getPose(bodyPart);
        return vp == null ? null : vp.offset();
    }
}

package org.vmstudio.visor.common.session;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.vmstudio.visor.api.player.VRPoseHistory;
import org.vmstudio.visor.protocol.value.PoseData;

public final class PoseHistoryBuffer {
    public record Snapshot(PoseData pose, double ox, double oy, double oz){}
    private final Deque<Snapshot> history = new ArrayDeque<>();

    public void push(PoseData pose, double ox, double oy, double oz){
        history.addFirst(new Snapshot(pose, ox, oy, oz));
        while(history.size() > VRPoseHistory.HISTORY_LIMIT){
            history.removeLast();
        }
    }

    /**
     * get an immutable copy of the snapshot history
     *
     * @return stored snapshots ordered from newest to oldest
     */
    public List<Snapshot> snapshot(){
        return List.copyOf(history);
    }

    public void clear(){
        history.clear();
    }
}

package org.vmstudio.visorplugin.common.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.vmstudio.visorplugin.api.player.VRPlayer;
import org.vmstudio.visorplugin.api.network.buffer.PoseDataBuffer;

@Getter
@Setter
public final class VrState {
    private final PoseHistoryBuffer poseHistory = new PoseHistoryBuffer();

    private static final String DEFAULT_BODY_TYPE = "null";

    private PoseDataBuffer pose;
    private boolean leftHanded;
    private boolean activeHandMain = true;
    private int offhandSlot;
    private String bodyType = DEFAULT_BODY_TYPE;
    private float worldScale = 1.0f;
    private float fullHeight = VRPlayer.DEFAULT_FULL_HEIGHT;
    private float rotationY;
    private float gunAngle = VRPlayer.DEFAULT_GUN_ANGLE;
    private boolean overlayFocused;
    private boolean crawling;

    private String bodyTypeLastSent;
    private boolean leftHandedLastSent;
    private float rotationYLastSent;
    private float worldScaleLastSent = 1.0f;
    private float fullHeightLastSent = VRPlayer.DEFAULT_FULL_HEIGHT;
    private float gunAngleLastSent = VRPlayer.DEFAULT_GUN_ANGLE;
    private boolean overlayFocusedLastSent;

    private Set<UUID> knownTrackers = new HashSet<>();

    public PoseDataBuffer pose(){
        return pose;
    }

    public void pushPoseSnapshot(PoseDataBuffer pose, double ox, double oy, double oz){
        poseHistory.push(pose, ox, oy, oz, rotationY);
    }

    public List<PoseHistoryBuffer.Snapshot> getPoseHistory(){
        return poseHistory.snapshot();
    }

    public void setBodyType(String v){
        this.bodyType = v == null ? DEFAULT_BODY_TYPE : v;
    }

}

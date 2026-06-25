package org.vmstudio.visor.common.session;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.vmstudio.visor.protocol.value.PoseData;

public final class VrState {
    public static final float DEFAULT_FULL_HEIGHT = 1.52f;
    public static final float DEFAULT_GUN_ANGLE = 60.0f;
    private static final String DEFAULT_BODY_TYPE = "null";

    private PoseData pose;
    private boolean leftHanded;
    private boolean activeHandMain = true;
    private int offhandSlot;
    private String bodyType = DEFAULT_BODY_TYPE;
    private float worldScale = 1.0f;
    private float fullHeight = DEFAULT_FULL_HEIGHT;
    private float rotationY;
    private float gunAngle = DEFAULT_GUN_ANGLE;
    private boolean overlayFocused;
    private boolean crawling;

    private String bodyTypeLastSent;
    private boolean leftHandedLastSent;
    private float worldScaleLastSent = 1.0f;
    private float fullHeightLastSent = DEFAULT_FULL_HEIGHT;
    private float gunAngleLastSent = DEFAULT_GUN_ANGLE;
    private boolean overlayFocusedLastSent;

    private Set<UUID> knownTrackers = new HashSet<>();

    public PoseData pose(){
        return pose;
    }

    public void setPose(PoseData pose){
        this.pose = pose;
    }

    public boolean leftHanded(){
        return leftHanded;
    }

    public void setLeftHanded(boolean v){
        this.leftHanded = v;
    }

    public boolean activeHandMain(){
        return activeHandMain;
    }

    public void setActiveHandMain(boolean v){
        this.activeHandMain = v;
    }

    public int offhandSlot(){
        return offhandSlot;
    }

    public void setOffhandSlot(int v){
        this.offhandSlot = v;
    }

    public String bodyType(){
        return bodyType;
    }

    public void setBodyType(String v){
        this.bodyType = v == null ? "" : v;
    }

    public float worldScale(){
        return worldScale;
    }

    public void setWorldScale(float v){
        this.worldScale = v;
    }

    public float fullHeight(){
        return fullHeight;
    }

    public void setFullHeight(float v){
        this.fullHeight = v;
    }

    public float rotationY(){
        return rotationY;
    }

    public void setRotationY(float v){
        this.rotationY = v;
    }

    public float gunAngle(){
        return gunAngle;
    }

    public void setGunAngle(float v){
        this.gunAngle = v;
    }

    public boolean overlayFocused(){
        return overlayFocused;
    }

    public void setOverlayFocused(boolean v){
        this.overlayFocused = v;
    }

    public boolean crawling(){
        return crawling;
    }

    public void setCrawling(boolean v){
        this.crawling = v;
    }

    public String bodyTypeLastSent(){
        return bodyTypeLastSent;
    }

    public void setBodyTypeLastSent(String v){
        this.bodyTypeLastSent = v;
    }

    public boolean leftHandedLastSent(){
        return leftHandedLastSent;
    }

    public void setLeftHandedLastSent(boolean v){
        this.leftHandedLastSent = v;
    }

    public float worldScaleLastSent(){
        return worldScaleLastSent;
    }

    public void setWorldScaleLastSent(float v){
        this.worldScaleLastSent = v;
    }

    public float fullHeightLastSent(){
        return fullHeightLastSent;
    }

    public void setFullHeightLastSent(float v){
        this.fullHeightLastSent = v;
    }

    public float gunAngleLastSent(){
        return gunAngleLastSent;
    }

    public void setGunAngleLastSent(float v){
        this.gunAngleLastSent = v;
    }

    public boolean overlayFocusedLastSent(){
        return overlayFocusedLastSent;
    }

    public void setOverlayFocusedLastSent(boolean v){
        this.overlayFocusedLastSent = v;
    }

    public Set<UUID> knownTrackers(){
        return knownTrackers;
    }

    public void setKnownTrackers(Set<UUID> trackers){
        this.knownTrackers = trackers;
    }
}

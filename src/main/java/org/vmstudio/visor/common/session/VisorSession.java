package org.vmstudio.visor.common.session;

import org.vmstudio.visor.common.platform.PlatformPlayer;

public final class VisorSession {
    private final PlatformPlayer player;
    private final boolean vrActive;
    private final String clientVersion;
    private final VrState vr;

    public VisorSession(PlatformPlayer player, boolean vrActive, String clientVersion){
        this.player = player;
        this.vrActive = vrActive;
        this.clientVersion = clientVersion;
        this.vr = vrActive ? new VrState() : null;
    }

    public PlatformPlayer player(){
        return player;
    }

    public boolean vrActive(){
        return vrActive;
    }

    public String clientVersion(){
        return clientVersion;
    }

    public VrState vr(){
        return vr;
    }
}

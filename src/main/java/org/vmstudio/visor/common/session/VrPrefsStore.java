package org.vmstudio.visor.common.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VrPrefsStore {
    private final ConcurrentHashMap<UUID, VrPrefs> prefs = new ConcurrentHashMap<>();

    public VrPrefs get(UUID uuid){
        return prefs.getOrDefault(uuid, VrPrefs.DEFAULT);
    }

    public void setRotationY(UUID uuid, float rotationY){
        prefs.compute(uuid, (k, v) ->
                v == null ? new VrPrefs(rotationY, VrPrefs.DEFAULT.offhandSlot())
                          : new VrPrefs(rotationY, v.offhandSlot()));
    }

    public void setOffhandSlot(UUID uuid, int offhandSlot){
        prefs.compute(uuid, (k, v) ->
                v == null ? new VrPrefs(VrPrefs.DEFAULT.rotationY(), offhandSlot)
                          : new VrPrefs(v.rotationY(), offhandSlot));
    }

    public Map<UUID, VrPrefs> snapshot(){
        return Map.copyOf(prefs);
    }

    public void loadAll(Map<UUID, VrPrefs> saved){
        prefs.putAll(saved);
    }
}

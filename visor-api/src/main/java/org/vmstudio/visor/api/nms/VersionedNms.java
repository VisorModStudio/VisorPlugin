package org.vmstudio.visor.api.nms;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public final class VersionedNms {
    private VersionedNms(){}

    public record Target(String className, int major, int minor, int minPatch, int maxPatch){
        public boolean matches(McVersion v){
            return v.major() == major && v.minor() == minor
                    && v.patch() >= minPatch && v.patch() <= maxPatch;
        }
    }

    @Nullable
    public static <T> T resolve(Class<T> type, McVersion version, List<Target> targets){
        for(Target target : targets){
            if(!target.matches(version)){
                continue;
            }
            try {
                Object impl = Class.forName(target.className()).getDeclaredConstructor().newInstance();
                return type.cast(impl);
            }catch (Throwable notUsable){
                return null;
            }
        }
        return null;
    }
}

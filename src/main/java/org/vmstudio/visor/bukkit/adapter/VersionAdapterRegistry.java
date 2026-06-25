package org.vmstudio.visor.bukkit.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.vmstudio.visor.common.platform.VisorLogger;
import org.vmstudio.visor.nms.McVersion;
import org.vmstudio.visor.nms.VersionAdapter;

public final class VersionAdapterRegistry {
    private final List<VersionAdapter> adapters = new ArrayList<>();

    public void register(VersionAdapter adapter){
        adapters.add(adapter);
    }

    public VersionAdapter select(McVersion version){
        return adapters.stream()
                .filter(a -> a.supports(version) && a.selfTest())
                .max(Comparator.comparingInt(VersionAdapter::priority))
                .orElse(null);
    }

    private record Leaf(String className, int major, int minor, int minPatch, int maxPatch){
        boolean matches(McVersion v){
            return v.major() == major && v.minor() == minor
                    && v.patch() >= minPatch && v.patch() <= maxPatch;
        }
    }

    private static final Leaf[] NMS_LEAVES = {
            new Leaf("org.vmstudio.visor.nms.v1_20_R1.NmsV1_20_R1Adapter", 1, 20, 1, 1),
            new Leaf("org.vmstudio.visor.nms.v1_21_1.NmsV1_21_1Adapter", 1, 21, 0, 1),
            new Leaf("org.vmstudio.visor.nms.v1_21_3.NmsV1_21_3Adapter", 1, 21, 2, 3),
            new Leaf("org.vmstudio.visor.nms.v1_21_4.NmsV1_21_4Adapter", 1, 21, 4, 4),
            new Leaf("org.vmstudio.visor.nms.v1_21_5.NmsV1_21_5Adapter", 1, 21, 5, 5),
            new Leaf("org.vmstudio.visor.nms.v1_21_6.NmsV1_21_6Adapter", 1, 21, 6, 6),
            new Leaf("org.vmstudio.visor.nms.v1_21_7.NmsV1_21_7Adapter", 1, 21, 7, 7),
            new Leaf("org.vmstudio.visor.nms.v1_21_8.NmsV1_21_8Adapter", 1, 21, 8, 8),
            new Leaf("org.vmstudio.visor.nms.v1_21_9.NmsV1_21_9Adapter", 1, 21, 9, 9),
            new Leaf("org.vmstudio.visor.nms.v1_21_10.NmsV1_21_10Adapter", 1, 21, 10, 10),
            new Leaf("org.vmstudio.visor.nms.v1_21_11.NmsV1_21_11Adapter", 1, 21, 11, 11),
    };

    public static VersionAdapter resolve(McVersion version, VisorLogger logger){
        VersionAdapterRegistry registry = new VersionAdapterRegistry();
        registry.register(new PaperApiAdapter(logger));
        for(Leaf leaf : NMS_LEAVES){
            if(!leaf.matches(version)){
                continue;
            }
            VersionAdapter adapter = tryLoadLeaf(leaf.className());
            if(adapter != null){
                registry.register(adapter);
            }
        }
        VersionAdapter selected = registry.select(version);
        return selected != null ? selected : new PaperApiAdapter(logger);
    }

    private static VersionAdapter tryLoadLeaf(String className){
        try {
            Class<?> clazz = Class.forName(className);
            return (VersionAdapter) clazz.getDeclaredConstructor().newInstance();
        }catch (Throwable notPresent){
            return null;
        }
    }
}

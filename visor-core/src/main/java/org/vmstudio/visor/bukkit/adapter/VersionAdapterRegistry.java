package org.vmstudio.visor.bukkit.adapter;

import java.util.List;

import org.vmstudio.visor.api.nms.McVersion;
import org.vmstudio.visor.api.nms.VersionedNms;
import org.vmstudio.visor.common.platform.VisorLogger;

public final class VersionAdapterRegistry {
    private VersionAdapterRegistry(){}

    private static final List<VersionedNms.Target> NMS_LEAVES = List.of(
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_20_R1.NmsV1_20_R1Adapter", 1, 20, 1, 1),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_1.NmsV1_21_1Adapter", 1, 21, 0, 1),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_3.NmsV1_21_3Adapter", 1, 21, 2, 3),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_4.NmsV1_21_4Adapter", 1, 21, 4, 4),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_5.NmsV1_21_5Adapter", 1, 21, 5, 5),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_6.NmsV1_21_6Adapter", 1, 21, 6, 6),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_7.NmsV1_21_7Adapter", 1, 21, 7, 7),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_8.NmsV1_21_8Adapter", 1, 21, 8, 8),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_9.NmsV1_21_9Adapter", 1, 21, 9, 9),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_10.NmsV1_21_10Adapter", 1, 21, 10, 10),
            new VersionedNms.Target("org.vmstudio.visor.nms.v1_21_11.NmsV1_21_11Adapter", 1, 21, 11, 11),
            new VersionedNms.Target("org.vmstudio.visor.nms.v26_1.NmsV26_1Adapter", 26, 1, 0, 2),
            new VersionedNms.Target("org.vmstudio.visor.nms.v26_2.NmsV26_2Adapter", 26, 2, 0, 0)
    );

    public static VisorVersionAdapter resolve(McVersion version, VisorLogger logger){
        VisorVersionAdapter leaf = VersionedNms.resolve(VisorVersionAdapter.class, version, NMS_LEAVES);
        if(leaf != null && leaf.selfTest()){
            return leaf;
        }
        return new PaperApiAdapter(logger);
    }
}

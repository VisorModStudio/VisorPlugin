package org.vmstudio.visor.common.settings;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

import org.vmstudio.visor.common.platform.VisorLogger;
import org.yaml.snakeyaml.Yaml;

public final class SettingsLoader {
    private SettingsLoader(){}

    public static VisorSettings load(Path file, VisorLogger logger){
        try {
            if(!Files.exists(file)){
                writeDefault(file);
                return VisorSettings.defaults();
            }
            try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)){
                Object raw = new Yaml().load(reader);
                if(!(raw instanceof Map<?, ?> map)){
                    return VisorSettings.defaults();
                }
                return fromMap(map);
            }
        }catch (Exception e){
            logger.warn("Failed to read {}, using defaults: {}", file, e.toString());
            return VisorSettings.defaults();
        }
    }

    private static VisorSettings fromMap(Map<?, ?> m){
        VisorSettings d = VisorSettings.defaults();
        return new VisorSettings(
                bool(m, "serverDebug", d.serverDebug()),
                bool(m, "vrOnly", d.vrOnly()),
                bool(m, "twoHandedVR", d.twoHandedVR()),
                bool(m, "betterSwinging", d.betterSwinging()),
                lng(m, "swingingRepairDelay", d.swingingRepairDelay()),
                bool(m, "roomCrawlingSupported", d.roomCrawlingSupported()),
                bool(m, "roomClimbingSupported", d.roomClimbingSupported()),
                bool(m, "pvpVRvsVanilla", d.pvpVRvsVanilla()),
                bool(m, "pvpVRvsVR", d.pvpVRvsVR()),
                bool(m, "notifyPvpBlocked", d.notifyPvpBlocked()),
                dbl(m, "creeperSwellDistance", d.creeperSwellDistance()),
                movement(m, "supportedMovement", d.supportedMovement()),
                intv(m, "teleportUpLimit", d.teleportUpLimit()),
                intv(m, "teleportDownLimit", d.teleportDownLimit()),
                intv(m, "teleportForwardLimit", d.teleportForwardLimit()));
    }

    private static boolean bool(Map<?, ?> m, String key, boolean def){
        Object v = m.get(key);
        return v instanceof Boolean b ? b : def;
    }

    private static long lng(Map<?, ?> m, String key, long def){
        Object v = m.get(key);
        return v instanceof Number n ? n.longValue() : def;
    }

    private static int intv(Map<?, ?> m, String key, int def){
        Object v = m.get(key);
        return v instanceof Number n ? n.intValue() : def;
    }

    private static double dbl(Map<?, ?> m, String key, double def){
        Object v = m.get(key);
        return v instanceof Number n ? n.doubleValue() : def;
    }

    private static SupportedMovement movement(Map<?, ?> m, String key, SupportedMovement def){
        Object v = m.get(key);
        if(v == null){
            return def;
        }
        try {
            return SupportedMovement.valueOf(v.toString().trim().toUpperCase(Locale.ROOT));
        }catch (IllegalArgumentException e){
            return def;
        }
    }

    private static void writeDefault(Path file) throws IOException {
        if(file.getParent() != null){
            Files.createDirectories(file.getParent());
        }
        try (Writer w = Files.newBufferedWriter(file, StandardCharsets.UTF_8)){
            w.write(DEFAULT_YAML);
        }
    }

    private static final String DEFAULT_YAML = """
            # Visor server settings
            serverDebug: false
            vrOnly: false

            twoHandedVR: false
            betterSwinging: true
            swingingRepairDelay: 400

            roomCrawlingSupported: true
            roomClimbingSupported: true

            pvpVRvsVanilla: true
            pvpVRvsVR: true
            notifyPvpBlocked: false

            creeperSwellDistance: 1.75

            # CONTROLLER | TELEPORT | BOTH
            supportedMovement: BOTH

            teleportUpLimit: 1
            teleportDownLimit: 4
            teleportForwardLimit: 16
            """;
}

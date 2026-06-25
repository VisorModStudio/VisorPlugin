package org.vmstudio.visor.bukkit;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.vmstudio.visor.common.session.VrPrefs;

final class PrefsFile {
    private PrefsFile(){}

    static Map<UUID, VrPrefs> load(Path file){
        Map<UUID, VrPrefs> out = new HashMap<>();
        if(!Files.exists(file)){
            return out;
        }
        Properties props = new Properties();
        try(Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)){
            props.load(reader);
        } catch(IOException e){
            return out;
        }
        for(String key : props.stringPropertyNames()){
            try {
                String[] parts = props.getProperty(key).split(",");
                out.put(UUID.fromString(key),
                        new VrPrefs(Float.parseFloat(parts[0]), Integer.parseInt(parts[1])));
            } catch(RuntimeException ignored){}
        }
        return out;
    }

    static void save(Path file, Map<UUID, VrPrefs> prefs){
        StringBuilder sb = new StringBuilder();
        sb.append("# Visor VR player prefs: rotationY,offhandSlot\n");
        sb.append("# Don't touch this!\n");
        prefs.forEach((uuid, p) -> sb.append(uuid).append('=')
                .append(p.rotationY()).append(',').append(p.offhandSlot()).append('\n'));
        try {
            if(file.getParent() != null){
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, sb.toString(), StandardCharsets.UTF_8);
        }catch(IOException e){
            System.err.println("[VisorPlugin] failed to save vr_prefs: " + e);
        }
    }
}

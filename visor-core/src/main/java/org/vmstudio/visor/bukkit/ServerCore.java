package org.vmstudio.visor.bukkit;

public enum ServerCore {
    BUKKIT,
    SPIGOT,
    PAPER,
    FOLIA;

    public boolean isPaper(){
        return this == PAPER || this == FOLIA;
    }

    public boolean isFolia(){
        return this == FOLIA;
    }

    public static ServerCore detect(){
        if(present("io.papermc.paper.threadedregions.RegionizedServer")){
            return FOLIA;
        }
        if(present("com.destroystokyo.paper.PaperConfig")
                || present("io.papermc.paper.configuration.GlobalConfiguration")){
            return PAPER;
        }
        if(present("org.spigotmc.SpigotConfig")){
            return SPIGOT;
        }
        return BUKKIT;
    }

    private static boolean present(String className){
        try {
            Class.forName(className);
            return true;
        }catch (Throwable t){
            return false;
        }
    }
}

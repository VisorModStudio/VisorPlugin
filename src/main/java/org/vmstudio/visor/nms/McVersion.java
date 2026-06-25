package org.vmstudio.visor.nms;

public record McVersion(int major, int minor, int patch, String raw){
    public static McVersion parse(String bukkitVersion){
        String v = bukkitVersion == null ? "" : bukkitVersion;
        int dash = v.indexOf('-');
        if(dash >= 0){
            v = v.substring(0, dash);
        }
        String[] parts = v.split("\\.");
        return new McVersion(part(parts, 0), part(parts, 1), part(parts, 2), v);
    }

    private static int part(String[] parts, int i){
        if(i >= parts.length){
            return 0;
        }
        try {
            return Integer.parseInt(parts[i].trim());
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public boolean atLeast(int maj, int min, int pat){
        if(major != maj){
            return major > maj;
        }
        if(minor != min){
            return minor > min;
        }
        return patch >= pat;
    }

    public boolean inMinorRange(int major, int fromMinor, int toMinor){
        return this.major == major && this.minor >= fromMinor && this.minor <= toMinor;
    }

    @Override
    public String toString(){
        return raw;
    }
}

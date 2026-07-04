package org.vmstudio.visorplugin.api.network;

public final class VisorNetwork {
    private VisorNetwork() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static final String CHANNEL = "visor:channel";

    public static final int NETWORK_VERSION = 5; // 5: since Visor 0.5.0

}

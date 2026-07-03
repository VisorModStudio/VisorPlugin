package org.vmstudio.visorplugin.api.network;

public final class VisorNetwork {
    private VisorNetwork() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static final String CHANNEL = "visor:channel";

    public static final int NETWORK_VERSION = 4; // 4 since Visor 0.4.0

}

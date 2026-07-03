package org.vmstudio.visorplugin.api.network;

import org.jetbrains.annotations.NotNull;

public record ChannelID(@NotNull String namespace, @NotNull String path) {
    public static final char NAMESPACE_SEPARATOR = ':';
    public String asString(){
        return namespace + NAMESPACE_SEPARATOR + path;
    }
}

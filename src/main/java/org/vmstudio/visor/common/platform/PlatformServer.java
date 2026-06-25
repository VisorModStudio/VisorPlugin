package org.vmstudio.visor.common.platform;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PlatformServer {
    String minecraftVersion();

    boolean isPaper();

    boolean isFolia();

    Optional<PlatformPlayer> player(UUID uuid);

    Collection<PlatformPlayer> onlinePlayers();

    Scheduler scheduler();

    VisorLogger logger();
}

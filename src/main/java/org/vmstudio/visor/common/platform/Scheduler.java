package org.vmstudio.visor.common.platform;

public interface Scheduler {
    Cancellable runGlobalTimer(Runnable task, long periodTicks);

    void runForPlayer(PlatformPlayer player, Runnable task);

    void runForPlayerDelayed(PlatformPlayer player, Runnable task, long delayTicks);
}

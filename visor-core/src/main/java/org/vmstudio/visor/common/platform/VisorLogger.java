package org.vmstudio.visor.common.platform;

public interface VisorLogger {
    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Object... args);

    boolean debugEnabled();
}

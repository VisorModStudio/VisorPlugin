package org.vmstudio.visor.bukkit.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vmstudio.visor.common.platform.VisorLogger;

public final class BukkitLogger implements VisorLogger {
    private final Logger logger;
    private volatile boolean debug;

    public BukkitLogger(Logger logger){
        this.logger = logger;
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    @Override
    public void info(String message, Object... args){
        logger.info(format(message, args));
    }

    @Override
    public void warn(String message, Object... args){
        logger.warning(format(message, args));
    }

    @Override
    public void error(String message, Object... args){
        logger.log(Level.SEVERE, format(message, args));
    }

    @Override
    public boolean debugEnabled(){
        return debug;
    }

    private static String format(String message, Object... args){
        if(args == null || args.length == 0){
            return message;
        }
        StringBuilder sb = new StringBuilder(message.length() + 16);
        int argIndex = 0;
        for(int i = 0; i < message.length(); i++){
            char c = message.charAt(i);
            if(c == '{' && i + 1 < message.length() && message.charAt(i + 1) == '}' && argIndex < args.length){
                sb.append(args[argIndex++]);
                i++;
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

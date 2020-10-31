package com.geekhome.common.utils;

public class Sleeper {
    public static void trySleep(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException ignored) {
        }
    }
}

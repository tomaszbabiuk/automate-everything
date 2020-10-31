package com.geekhome.common.logging;

public class LoggingService {
    private static ILogger _logger;
    private static Throwable _lastServerError;

    public static Throwable getLastServerError() {
        return _lastServerError;
    }

    public static void setLastServerError(Throwable value) {
        _lastServerError = value;
    }

    public static ILogger getLogger() {
        if (_logger == null) {
            _logger = new Log4JLogger();
        }
        return _logger;
    }
}
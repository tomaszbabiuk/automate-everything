package com.geekhome.common.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Log4JLogger extends LoggerBase {

    private final String LOGGER_NAME = "errors";
    private final String LOG_MAIN_PATH = "logs";
    private final int DAYS_TO_KEEP_ERRORS = 30;
    private Logger _errorsLogger;
    private Logger _rootLogger = LogManager.getRootLogger();
    public Log4JLogger() {
        createErrorsLogger();
        _errorsLogger = LogManager.getLogger(LOGGER_NAME);
    }

    private void createErrorsLogger() {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();

        String todayLogPath = LOG_MAIN_PATH + File.separator + "today.log";
        String rolledLogPath = LOG_MAIN_PATH + File.separator + "%d{yyyy-MM-dd}.log";

        RolloverStrategy rolloverStrategyOld = DefaultRolloverStrategy.createStrategy("1", "1", "max", "0", new Action[]{
                new ErrorsCleanUpAction(DAYS_TO_KEEP_ERRORS, new File(LOG_MAIN_PATH))
        }, false, config);

        TriggeringPolicy triggeringPolicy = TimeBasedTriggeringPolicy.createPolicy("1", "false");
        Appender fileAppender = RollingFileAppender.createAppender(todayLogPath, rolledLogPath, "false",
                "Rolling file for events", "true", "4096", "true", triggeringPolicy, rolloverStrategyOld, null, null, "true",
                null, null, config);
        fileAppender.start();
        config.addAppender(fileAppender);

        LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.DEBUG, LOGGER_NAME,
                "true", new AppenderRef[0], null, config, null);
        loggerConfig.addAppender(fileAppender, Level.DEBUG, null);
        config.addLogger(LOGGER_NAME, loggerConfig);

        ctx.updateLoggers();
    }

    private Level matchLevel(LogLevel level) {
        switch (level) {
            case Verbose:
                return Level.ALL;
            case Warning:
                return Level.WARN;
            case Info:
            case Activity:
            case Value:
                return Level.INFO;
            case Alert:
                return Level.INFO;
            case Error:
                return Level.ERROR;
            default:
                return Level.DEBUG;
        }
    }

    @Override
    protected void log(TimedLog log) {
        super.log(log);
        _rootLogger.log(matchLevel(log.getLevel()), log.toString());
    }

    @Override
    protected void logError(ErrorLog log) {
        _errorsLogger.log(Level.INFO, log);
    }

    public class ErrorsCleanUpAction implements Action {
        private int _days;
        private File _logsDirectory;

        public ErrorsCleanUpAction(int days, File logsDirectory) {
            _days = days;
            _logsDirectory = logsDirectory;
        }

        @Override
        public boolean execute() throws IOException {
            long now = Calendar.getInstance().getTimeInMillis();
            long threeDaysMillis = TimeUnit.DAYS.toMillis(_days);

            File[] logFiles = _logsDirectory.listFiles();
            if (logFiles != null) {
                for (File logFile : logFiles) {
                    Path filePath = Paths.get(logFile.getAbsolutePath());
                    FileTime lastModifiedTime = Files.getLastModifiedTime(filePath, LinkOption.NOFOLLOW_LINKS);
                    long modificationDuration = now - lastModifiedTime.toMillis();

                    if (modificationDuration > threeDaysMillis) {
                        try {
                            Files.delete(filePath);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void close() {
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public void run() {
        }
    }
}

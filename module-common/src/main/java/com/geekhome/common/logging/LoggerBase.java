package com.geekhome.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class LoggerBase implements ILogger {
    private List<TimedLog> _truncatedLogs = new ArrayList<>();
    private List<TimedLog> _activities = new ArrayList<>();
    private List<ErrorLog> _errors = new ArrayList<>();
    private int _errorsCount;

    private Object _lock = new Object();

    @Override
    public List<TimedLog> getActivities() {
        return _activities;
    }

    @Override
    public List<ErrorLog> getErrors() {
        return _errors;
    }

    public void verbose(String message) {
        EventLog log = new EventLog(message, null, LogLevel.Verbose);
        log(log);
    }

    public void debug(String message) {
        EventLog log = new EventLog(message, null, LogLevel.Debug);
        log(log);
    }

    public void info(String message) {
        EventLog log = new EventLog(message, null, LogLevel.Info);
        log(log);
    }

    public void warning(String message) {
        EventLog log = new EventLog(message, null, LogLevel.Warning);
        log(log);
    }

    public void warning(String message, Throwable exception) {
        String details = stackTraceToString(exception);
        EventLog log = new EventLog(message, details, LogLevel.Warning);
        log(log);
    }

    public void error(String message, Throwable exception) {
        _errorsCount++;

        String details = stackTraceToString(exception);
        EventLog log = new EventLog(message, details, LogLevel.Error);
        log(log);
        logError();
    }

    private void logError() {
        List<TimedLog> stack = new ArrayList<>();
        stack.addAll(_truncatedLogs);
        ErrorLog errorLog = new ErrorLog(stack);
        addAndTrim(10, errorLog, _errors);
        logError(errorLog);
        _truncatedLogs.clear();
    }

    private String stackTraceToString(Throwable exception) {
        StringWriter stack = new StringWriter();
        exception.printStackTrace(new PrintWriter(stack));
        return stack.toString();
    }

    protected <T> void addAndTrim(int maxCollectionSize, T log, List<T> targetList) {
        synchronized (_lock) {
            targetList.add(log);
            if (targetList.size() > maxCollectionSize) {
                targetList.remove(0);
            }
        }
    }

    protected <T> void insertAndTrim(int maxCollectionSize, T log, List<T> targetList) {
        synchronized (_lock) {
            targetList.add(0, log);
            if (targetList.size() > maxCollectionSize) {
                targetList.remove(targetList.size() - 1);
            }
        }
    }

    @Override
    public <T> void activity(String source, T value, T prevValue) {
        ActivityLog<T> log = new ActivityLog<>(source, value, prevValue);
        log(log);

        addAndTrim(50, log, _activities);
    }

    protected void log(TimedLog log) {
        insertAndTrim(50, log, _truncatedLogs);
    }

    protected abstract void logError(ErrorLog log);

    @Override
    public void clearActivities() {
        _activities.clear();
        _errorsCount = 0;
    }

    @Override
    public <T> void valueChange(String source, T value) {
        ValueLog<T> log = new ValueLog<>(source, value);
        log(log);

        addAndTrim(50, log, _activities);
    }

    @Override
    public int getErrorsCount() {
        return _errorsCount;
    }
}
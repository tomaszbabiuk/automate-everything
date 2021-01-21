package com.geekhome.common.settings;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;

import java.util.Collection;
import java.util.Hashtable;

public class AutomationSettings {
    public static final String TABLE_DEVICES = "Devices";

    private static final ILogger Logger = LoggingService.getLogger();
    private final TextFileAutomationSettingsPersister _persister;
    private final Hashtable<String, Hashtable<String, String>> _tables;

    private void onModified() {
        _persister.save(_tables);
    }

    public Hashtable<String, String> getSettings() {
        return _tables.get("Settings");
    }

    private Hashtable<String, String> getTable(String tableName) {
        if (_tables.containsKey(tableName)) {
            return _tables.get(tableName);
        } else {
            Hashtable<String, String> newTable = new Hashtable<>();
            _tables.put(tableName, newTable);
            return newTable;
        }
    }

    public AutomationSettings(TextFileAutomationSettingsPersister persister) throws Exception {
        _persister = persister;
        _tables = new Hashtable<>();
        _tables.put("Settings", new Hashtable<String, String>());
        _persister.load(_tables);
    }

    public void changeSetting(String name, String value) {
        changeSetting("Settings", name, value);
    }

    public void changeSetting(String tableName, String name, String value) {
        Logger.debug(String.format("Changing automation setting. Section: %s, name: %s, value: %s ", tableName, name, value));
        Hashtable<String, String> table = getTable(tableName);
        table.put(name, value);
        onModified();
    }

    public void clearSetting(String tableName, String name) {
        Logger.debug(String.format("Clearing automation setting. Section: %s, name: %s", tableName, name));
        Hashtable<String, String> table = getTable(tableName);
        table.remove(name);
        onModified();
    }

    public String readSetting(String settingName) {
        return readSetting(settingName, "");
    }

    public String readSetting(String settingName, String defaultValue) {
        String settingFromFile = "";
        if (getSettings().containsKey(settingName)) {
            settingFromFile = getSettings().get(settingName);
        }

        return settingFromFile.equals("") ? defaultValue : settingFromFile;
    }

    public Collection<String> getTableKeys(String tableName) {
        return getTable(tableName).keySet();
    }

    public String readTable(String tableName, String key) {
        return getTable(tableName).get(key);
    }
}
package com.geekhome.common.localization;

public interface ILocalizationProvider {
    String getValue(String key);
    void load(Resource[] resources);
}

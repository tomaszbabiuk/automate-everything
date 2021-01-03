package com.geekhome.common.configuration;

import java.util.List;

public class ConfigInfo {
    private final MasterConfiguration _masterConfiguration;

    public ConfigInfo(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    @Persistable(name="IsConfigurationModified")
    public boolean isConfigurationModified() {
        return _masterConfiguration.isAnyCollectionerModified();
    }

    @Persistable(name="Validations")
    public List<String> getValidations() {
        return _masterConfiguration.getVallidations();
    }
}
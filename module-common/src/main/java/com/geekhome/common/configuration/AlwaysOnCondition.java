package com.geekhome.common.configuration;

import com.geekhome.common.localization.ILocalizationProvider;

public class AlwaysOnCondition extends ConditionBase {
    public AlwaysOnCondition(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName(localizationProvider.getValue("C:AlwaysOn"), "alwayson"));
    }
}
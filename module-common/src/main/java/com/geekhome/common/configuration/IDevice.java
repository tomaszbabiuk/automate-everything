package com.geekhome.common.configuration;

import com.geekhome.common.DeviceCategory;
import com.geekhome.common.ControlType;
import com.geekhome.common.INamedObject;
import com.geekhome.common.localization.ILocalizationProvider;

public interface IDevice extends INamedObject {
    String getIconName();
    ControlType getControlType();
    DeviceCategory getCategory();
    String getGroupName(ILocalizationProvider localizationProvider);
}

package com.geekhome.common.configuration;

import com.geekhome.common.*;
import com.geekhome.common.localization.ILocalizationProvider;

public abstract class MultistateDevice extends Device {
    public abstract CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider);

    @Persistable(name="HasNonReadonlyStates")
    public abstract boolean hasNonReadonlyStates();

    protected MultistateDevice(DescriptiveName name, String iconName, DeviceCategory deviceCategory) {
        super(name, iconName, deviceCategory);
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.Multistate;
    }
}


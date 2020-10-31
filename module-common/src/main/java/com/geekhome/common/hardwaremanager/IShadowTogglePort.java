package com.geekhome.common.hardwaremanager;

public interface IShadowTogglePort extends IShadowPort, ITogglePort {
    void setTarget(ITogglePort target);
}

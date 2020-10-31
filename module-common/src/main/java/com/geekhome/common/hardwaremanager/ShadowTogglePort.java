package com.geekhome.common.hardwaremanager;

import com.geekhome.common.ConnectiblePortBase;
import com.geekhome.common.IConnectible;

import java.util.Calendar;

public class ShadowTogglePort<T> extends ConnectiblePortBase implements IShadowTogglePort {

    private ITogglePort _target;
    private ITogglePort _base;

    public ShadowTogglePort(String id, ITogglePort base) {
        super(id, 0);
        _base = base;
    }

    public void setTarget(ITogglePort target) {
        _target = target;
    }

    @Override
    public void toggleOn() throws Exception {
        if (_target != null) {
            _target.toggleOn();
        }

        _base.toggleOn();
    }

    @Override
    public void toggleOff() throws Exception {
        if (_target != null) {
            _target.toggleOff();
        }

        _base.toggleOff();
    }

    @Override
    public boolean hasTarget() {
        return _target != null;
    }

    @Override
    public boolean isConnected(Calendar now) {
        if (_target != null && _target instanceof IConnectible) {
            return ((IConnectible)_target).isConnected(now);
        }

        return false;
    }
}
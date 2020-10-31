package com.geekhome.common.hardwaremanager;

import com.geekhome.common.ConnectiblePortBase;
import com.geekhome.common.IConnectible;

import java.util.Calendar;

public class ShadowInputPort<T> extends ConnectiblePortBase implements IShadowInputPort<T> {

    private IInputPort<T> _target;
    private IInputPort<T> _base;

    public ShadowInputPort(String id, IInputPort<T> base) {
        super(id, 0);
        _base = base;
    }

    public void setTarget(IInputPort<T> target) {
        _target = target;
    }

    @Override
    public boolean hasTarget() {
        return _target != null;
    }

    @Override
    public T read() {
        if (_target != null) {
            return _target.read();
        }

        return _base.read();
    }

    @Override
    public boolean isConnected(Calendar now) {
        if (_target != null && _target instanceof IConnectible) {
            return ((IConnectible)_target).isConnected(now);
        }

        return false;
    }
}

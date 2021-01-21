package com.geekhome.common.hardwaremanager;

import com.geekhome.common.ConnectiblePortBase;
import com.geekhome.common.IConnectible;

import java.util.Calendar;

public class ShadowOutputPort<T> extends ConnectiblePortBase implements IShadowOutputPort<T> {

    private IOutputPort<T> _target;
    private final IOutputPort<T> _base;

    public ShadowOutputPort(String id, IOutputPort<T> base) {
        super(id, 0);
        _base = base;
    }

    public void setTarget(IOutputPort<T> target) {
        _target = target;
    }

    @Override
    public void write(T value) throws Exception {
        if (_target != null) {
            _target.write(value);
        } else {
            _base.write(value);
        }
    }

    @Override
    public T read() {
        if (_target != null) {
            return _target.read();
        }

        return _base.read();
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

package com.geekhome.common.hardwaremanager;

import java.util.Hashtable;

public class InputPortsCollection<T> extends Hashtable<String, IInputPort<T>> {
    private final PortType _type;

    public InputPortsCollection(PortType type) {
        _type = type;
    }

    public void add(IInputPort<T> port) {
        String portId = port.getId();
        if (containsKey(portId)) {
            IInputPort<T> existingPort = get(portId);
            if (existingPort instanceof IShadowInputPort) {
                ((IShadowInputPort<T>) existingPort).setTarget(port);
            }
        } else {
            put(port.getId(), port);
        }
    }

    public IInputPort<T> find(String uniqueId) throws PortNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        throw new PortNotFoundException(uniqueId, _type);
    }

    public IInputPort<T> tryFind(String uniqueId) {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        return null;
    }
}


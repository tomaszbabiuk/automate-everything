package com.geekhome.common.hardwaremanager;

import java.util.Hashtable;

public class OutputPortsCollection<T> extends Hashtable<String, IOutputPort<T>> {
    private PortType _type;

    public OutputPortsCollection(PortType type) {
        _type = type;
    }

    public void add(IOutputPort<T> port) {
        String portId = port.getId();
        if (containsKey(portId)) {
            IOutputPort<T> existingPort = get(portId);
            if (existingPort instanceof IShadowOutputPort) {
                ((IShadowOutputPort<T>) existingPort).setTarget(port);
            }
        } else {
            put(port.getId(), port);
        }
    }


    public IOutputPort<T> tryFind(String uniqueId) {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        return null;
    }

    public IOutputPort<T> find(String uniqueId) throws PortNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        throw new PortNotFoundException(uniqueId, _type);
    }
}


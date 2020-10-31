package com.geekhome.common.hardwaremanager;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.Hashtable;

public class InputPortsCollection<T> extends Hashtable<String, IInputPort<T>> implements JSONAware {
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

    @Override
    public String toJSONString() {
        JSONArray jsons = new JSONArray();
        for(IInputPort<T> port : values()) {
            jsons.add(port);
        }

        return jsons.toJSONString();
    }
}


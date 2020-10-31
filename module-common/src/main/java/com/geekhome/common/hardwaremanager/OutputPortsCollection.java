package com.geekhome.common.hardwaremanager;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.Hashtable;

public class OutputPortsCollection<T> extends Hashtable<String, IOutputPort<T>> implements JSONAware {
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

    @Override
    public String toJSONString() {
        JSONArray jsons = new JSONArray();
        for(IOutputPort<T> port : values()) {
            jsons.add(port);
        }

        return jsons.toJSONString();
    }
}


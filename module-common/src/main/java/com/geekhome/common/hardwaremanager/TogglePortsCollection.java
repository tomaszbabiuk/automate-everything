package com.geekhome.common.hardwaremanager;

import java.util.Hashtable;

public class TogglePortsCollection extends Hashtable<String, ITogglePort> {
    private PortType _type;

    public TogglePortsCollection(PortType type) {
        _type = type;
    }

    public void add(ITogglePort port) {
        String portId = port.getId();
        if (containsKey(portId)) {
            ITogglePort existingPort = get(portId);
            if (existingPort instanceof IShadowTogglePort) {
                ((IShadowTogglePort) existingPort).setTarget(port);
            }
        } else {
            put(port.getId(), port);
        }
    }

    public ITogglePort find(String uniqueId) throws PortNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        throw new PortNotFoundException(uniqueId, _type);
    }

    public ITogglePort tryFind(String uniqueId) {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        return null;
    }
}


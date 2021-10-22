package eu.automateeverything.onewireplugin;

class AdapterTask {
    private TaskType _type;
    private DiscoveryInfo _discoveryInfo;

    TaskType getType() {
        return _type;
    }

    DiscoveryInfo getDiscoveryInfo() {
        return _discoveryInfo;
    }

    AdapterTask(TaskType type, DiscoveryInfo discoveryInfo) {
        _type = type;
        _discoveryInfo = discoveryInfo;
    }
}

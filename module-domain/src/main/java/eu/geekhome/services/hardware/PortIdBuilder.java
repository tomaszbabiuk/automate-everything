package eu.geekhome.services.hardware;

public class PortIdBuilder {

    private final String _factoryId;
    private final String _adapterId;

    public PortIdBuilder(String factoryId, String adapterId) {
        _factoryId = factoryId;
        _adapterId = adapterId;
    }

    public String buildPortId(String portPrefix, int channel, String portSuffix) {
        return _factoryId + "-" + _adapterId + ":" + portPrefix + ":" + portSuffix + "-" + channel;
    }
}

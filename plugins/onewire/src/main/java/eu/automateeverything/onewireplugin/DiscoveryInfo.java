package eu.automateeverything.onewireplugin;

public class DiscoveryInfo<C extends IOneWireContainer> {
    private String _name;
    private String _address;
    private C _container;

    public String getName() {
        return _name;
    }

    public void setName(String value) {
        _name = value.toUpperCase();
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String value) {
        _address = value;
    }

    public C getContainer() {
        return _container;
    }

    protected DiscoveryInfo(C container) {
        _container = container;
        setName(container.getNameLowercase());
        setAddress(container.getAddressAsString());
    }

    @Override
    public String toString() {
        return String.format("1-wire device %s [%s]", getName(), getAddress());
    }
}
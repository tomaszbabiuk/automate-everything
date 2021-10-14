package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.container.OneWireContainer;

class ContainerWrapperBase implements IOneWireContainer {

    private final String _name;
    private final String _address;

    ContainerWrapperBase(OneWireContainer container) {
        _name = container.getName().toLowerCase();
        _address = container.getAddressAsString();
    }

    @Override
    public String getNameLowercase() {
        return _name;
    }

    @Override
    public String getAddressAsString() {
        return _address;
    }
}

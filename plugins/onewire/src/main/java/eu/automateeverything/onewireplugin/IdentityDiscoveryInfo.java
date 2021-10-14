package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.container.OneWireContainer01;

class IdentityDiscoveryInfo extends DiscoveryInfo<IdentityContainerWrapper> {
    IdentityDiscoveryInfo(OneWireContainer01 container) {
        super(new IdentityContainerWrapper(container));
    }
}

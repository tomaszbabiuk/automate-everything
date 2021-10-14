package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.container.OneWireContainer28;

public class TemperatureDiscoveryInfo extends DiscoveryInfo<TemperatureContainerWrapper> {
    private final Double _initialTemperature;

    public double getInitialTemperature() {
        return _initialTemperature;
    }

    public TemperatureDiscoveryInfo(OneWireContainer28 container) throws OneWireException {
        super(new TemperatureContainerWrapper(container));
        _initialTemperature = getContainer().readTemperature();
    }
}
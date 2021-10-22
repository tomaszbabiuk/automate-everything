package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.container.OneWireContainer28;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class TemperatureContainerWrapper extends ContainerWrapperBase {
    private OneWireContainer28 _wrapped;
    private long _lastTemperatureRead = 0;
    private Double _lastTemperature;

    TemperatureContainerWrapper(OneWireContainer28 temperatureContainer) throws OneWireException {
        super(temperatureContainer);
        _wrapped = temperatureContainer;
        _lastTemperatureRead = Calendar.getInstance().getTimeInMillis();
    }

    Double tryReadTemperature() throws AdapterNonOperationalException {
        long now = Calendar.getInstance().getTimeInMillis();
        if (_lastTemperature != null && now - _lastTemperatureRead < 30000) {
            return _lastTemperature;
        }

        try {
            if (now - _lastTemperatureRead > 120000) {
                throw new AdapterNonOperationalException();
            }

            Double temperature = _wrapped.tryReadTemperature();
            if (temperature != null) {
                _lastTemperature = temperature;
                _lastTemperatureRead = now;
            }
            return _lastTemperature;
        } catch (OneWireException ex) {
            System.out.println(String.format("Cannot read temperature of %s.", getAddressAsString()) + ex.getMessage());
            return null;
        }
    }

    Double readTemperature() throws OneWireException {
        for (int trial=1; trial<=3; trial++) {
            try {
                return readTemperatureInternal();
            } catch (Exception ex) {
                Sleeper.trySleep(750);
                if (trial == 3) {
                    System.out.println(String.format("Cannot read temperature of %s. Trial %d.", getAddressAsString(), trial));
                }
            }
        }

        return null;
    }

    private double readTemperatureInternal() throws OneWireException {
        byte[] state = _wrapped.readDevice();
        _wrapped.doTemperatureConvert(state);
        state = _wrapped.readDevice();
        BigDecimal lastTemp = new BigDecimal (_wrapped.getTemperature(state)).setScale(2, RoundingMode.CEILING);
        _lastTemperatureRead = Calendar.getInstance().getTimeInMillis();
        _lastTemperature = lastTemp.doubleValue();
        return _lastTemperature;
    }
}

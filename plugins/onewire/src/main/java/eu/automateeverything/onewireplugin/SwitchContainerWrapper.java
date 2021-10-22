package eu.automateeverything.onewireplugin;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.SwitchContainer;

import java.util.ArrayList;
import java.util.Arrays;

public class SwitchContainerWrapper extends ContainerWrapperBase {
    private boolean _lastReadingFailed;
    private boolean[] _values;
    private boolean[] _prevValues;
    private SwitchContainer _wrapped;
    private int _channelsCount;

    int getChannelsCount() {
        return _channelsCount;
    }

    SwitchContainerWrapper(SwitchContainer switchContainer) throws OneWireException {
        super((OneWireContainer)switchContainer);
        _wrapped = switchContainer;
        byte[] initialData = _wrapped.readDevice();
        _channelsCount = readChannelsCount(initialData);
        _values = new boolean[_channelsCount];
        _lastReadingFailed = false;
        readInitialValues(initialData);
        makeCopy();
    }

    private void makeCopy() {
        _prevValues = _values.clone();
    }

    private void readInitialValues(byte[] data) throws OneWireException {
        for (int i = 0; i < _values.length; i++) {
            _values[i] = !_wrapped.getLatchState(i, data);
        }
    }

    Boolean read(int channel) {
        return _values[channel];
    }

    void write(int channel, Boolean value) throws Exception {
        _values[channel] = value;
    }

    ArrayList<SwitchContainerReading> tryRead(boolean sensing) {
        try {
            ArrayList<SwitchContainerReading> readings = readInternal(sensing);
            preserveSensedOnlyReadings(readings);
            _lastReadingFailed = false;
            return readings;
        } catch (Exception ex) {
            String msg = String.format("Cannot read device %s!", ((OneWireContainer) _wrapped).getAddressAsString());
            //TODO:_logger.warning(msg, ex);
            _lastReadingFailed = true;
            return null;
        }
    }

    private boolean preserveSensedOnlyReadings(ArrayList<SwitchContainerReading> readings) throws OneWireException {
        for (SwitchContainerReading reading : readings) {
            if (!reading.isSensed()) {
                return false;
            }
        }

        throw new OneWireException("All readings are SENSED! Ignoring...");
    }

    private ArrayList<SwitchContainerReading> readInternal(boolean sensing) throws OneWireException {
        if (_lastReadingFailed) {
            //TODO:_logger.info("Last reading has failed... disable sensing in this round!");
        }
        ArrayList<SwitchContainerReading> result = new ArrayList<>();
        byte[] state = _wrapped.readDevice();
        int channelsCount = _wrapped.getNumberChannels(state);
        if (sensing) {
            for (int channel = 0; channel < channelsCount; channel++) {
                _wrapped.setLatchState(channel, true, false, state);
            }
            _wrapped.writeDevice(state);
            for (int channel = 0; channel < channelsCount; channel++) {
                boolean level = _wrapped.getLevel(channel, state);
                boolean sensed = !_lastReadingFailed && _wrapped.getSensedActivity(channel, state);
                _wrapped.setLatchState(channel, !level, false, state);
                result.add(new SwitchContainerReading(channel, level, sensed));
            }
            _wrapped.clearActivity();
        } else {
            for (int channel = 0; channel < channelsCount; channel++) {
                boolean level = _wrapped.getLevel(channel, state);
                result.add(new SwitchContainerReading(channel, level, false));
            }
        }

        return result;
    }

    void tryExecute() throws OneWireException {
        int trial = 1;
        while (trial < 4) {
            if (tryExecute(trial)) {
                return;
            }

            trial++;
        }

        throw new OneWireException(String.format("Cannot write device %s!", ((OneWireContainer)_wrapped).getAddressAsString()));
    }

    private boolean tryExecute(int trialNo) {
        try {
            execute();
            return true;
        } catch (Exception ex) {
            Sleeper.trySleep(500);
            String msg = String.format("Cannot write device %s, trial %d!", ((OneWireContainer) _wrapped).getAddressAsString(), trialNo);
            //TODO:_logger.info(msg);
            return false;
        }
    }

    private void execute() throws OneWireException {
        boolean[] prevValuesSnapshot = _prevValues.clone();
        if (!Arrays.equals(_values, _prevValues)) {
            makeCopy();
            for (int i = 0; i < _values.length; i++) {
                if (_values[i] != prevValuesSnapshot[i]) {
                    //TODO:_logger.activity(getAddressAsString() + "-" + i, _values[i], prevValuesSnapshot[i]);
                    byte[] data = _wrapped.readDevice();
                    _wrapped.setLatchState(i, !_values[i], false, data);
                    _wrapped.writeDevice(data);
                }
            }
        }
    }

    private int readChannelsCount(byte[] data) throws OneWireException {
        try {
            return _wrapped.getNumberChannels(data);
        } catch (Exception ex) {
            return 0;
        }
    }

    class SwitchContainerReading {
        private int _channel;
        private boolean _level;
        private boolean _sensed;

        int getChannel() {
            return _channel;
        }

        boolean getLevel() {
            return _level;
        }

        boolean isSensed() {
            return _sensed;
        }

        SwitchContainerReading(int channel, boolean level, boolean sensed) {
            _channel = channel;
            _level = level;
            _sensed = sensed;
        }
    }
}

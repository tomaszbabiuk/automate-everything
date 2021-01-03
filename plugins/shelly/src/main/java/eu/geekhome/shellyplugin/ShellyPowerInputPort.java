package eu.geekhome.shellyplugin;

public class ShellyPowerInputPort extends ShellyInputPort<Double> {
    private ShellyPowerInputPort(String shellyId, int channel, Double initialValue, long connectionLostInterval) {
        super(shellyId + "-PWR-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel + "/power", connectionLostInterval);
    }

    ShellyPowerInputPort(ShellySettingsResponse settingsResponse, int channel, long connectionLostInterval) {
        this(settingsResponse.getDevice().getHostname(), channel,
                settingsResponse.getMeters().get(channel).getPower(),
                connectionLostInterval);
    }

    @Override
    public void setValueFromMqttPayload(String payload) {
        double value = Double.parseDouble(payload);
        setValue(value);
    }
}

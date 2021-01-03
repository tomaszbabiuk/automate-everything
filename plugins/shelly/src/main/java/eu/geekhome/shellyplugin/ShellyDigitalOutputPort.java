package eu.geekhome.shellyplugin;

public class ShellyDigitalOutputPort extends ShellyOutputPort<Boolean> {

    private ShellyDigitalOutputPort(String shellyId, int channel, Boolean initialValue, long connectionLostInterval) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel,
                "shellies/" + shellyId + "/relay/" + channel+ "/command", connectionLostInterval);
    }

    ShellyDigitalOutputPort(ShellySettingsResponse settingsResponse, int channel, long connectionLostInterval) {
        this(settingsResponse.getDevice().getHostname(), channel, settingsResponse.getRelays().get(channel).isOn(),
                connectionLostInterval);
    }

    public String convertValueToMqttPayload() {
        return getValue() ? "on" : "off";
    }

    @Override
    public void setValueFromMqttPayload(String payload) {
        boolean value = payload != null && payload.equals("on");
        setValue(value);
    }
}

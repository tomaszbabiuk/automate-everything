package eu.geekhome.shellyplugin;

import com.google.gson.Gson;

public class ShellyPowerOutputPort extends ShellyOutputPort<Integer> {

    private final Gson _gson;

    ShellyPowerOutputPort(ShellySettingsResponse settingsResponse, ShellyLightResponse lightResponse,
                          int channel, long connectionLostInterval) {
        this(settingsResponse.getDevice().getHostname(), channel, calculateBrightness(lightResponse),
                connectionLostInterval);
    }

    private static int calculateBrightness(ShellyLightResponse lightResponse) {
        boolean isOn = lightResponse.isOn();
        return isOn ? lightResponse.getBrightness() : 0;
    }

    private ShellyPowerOutputPort(String shellyId, int channel, Integer initialValue, long connectionLostInterval) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/light/" + channel + "/status",
                "shellies/" + shellyId + "/light/" + channel + "/set",
                connectionLostInterval);

        _gson = new Gson();
    }

    public String convertValueToMqttPayload() {
        ShellyLightSet response = new ShellyLightSet();
        if (getValue() == 0) {
            response.setTurn("off");
        } else {
            response.setTurn("on");
            response.setBrightness(getValue());
        }

        return _gson.toJson(response);
    }

    @Override
    public void setValueFromMqttPayload(String payload) {
        ShellyLightResponse response = _gson.fromJson(payload, ShellyLightResponse.class);
        int value = calculateBrightness(response);
        setValue(value);
    }
}
package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShellySettingsResponse {

    @SerializedName("device")
    private ShellyDeviceBrief _device;

    @SerializedName("relays")
    private ArrayList<ShellyRelayBrief> _relays;

    @SerializedName("meters")
    private ArrayList<ShellyMeterBrief> _meters;

    @SerializedName("lights")
    private ArrayList<ShellyLightBrief> _lights;

    @SerializedName("mqtt")
    private ShellyMqttBrief _mqtt;

    @SerializedName("cloud")
    private ShellyCloudBrief _cloud;

    public ShellyDeviceBrief getDevice() {
        return _device;
    }

    public ArrayList<ShellyRelayBrief> getRelays() {
        return _relays;
    }

    public ArrayList<ShellyMeterBrief> getMeters() {
        return _meters;
    }

    public ShellyMqttBrief getMqtt() {
        return _mqtt;
    }

    public ShellyCloudBrief getCloud() {
        return _cloud;
    }

    public ArrayList<ShellyLightBrief> getLights() {
        return _lights;
    }
}

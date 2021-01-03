package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

/*
{"ison":false,"mode":"white","brightness":22}
 */
public class ShellyLightResponse {

    @SerializedName("ison")
    private boolean _isOn;

    @SerializedName("mode")
    private String _mode;

    @SerializedName("brightness")
    private int _brightness;

    public boolean isOn() {
        return _isOn;
    }

    public String getMode() {
        return _mode;
    }

    public int getBrightness() {
        return _brightness;
    }

    public void setBrightness(int brightness) {
        _brightness = brightness;
    }

    public void setOn(boolean on) {
        _isOn = on;
    }
}

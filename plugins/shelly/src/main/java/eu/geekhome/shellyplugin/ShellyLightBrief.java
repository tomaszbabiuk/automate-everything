package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

public class ShellyLightBrief {
    @SerializedName("ison")
    private boolean _isOn;

    public boolean isOn() {
        return _isOn;
    }
}

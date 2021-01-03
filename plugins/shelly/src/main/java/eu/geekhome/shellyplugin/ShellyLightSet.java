package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

/*
{"turn":"on", "brightness":22}
 */
public class ShellyLightSet {

    @SerializedName("turn")
    private String _turn;

    @SerializedName("brightness")
    private int _brightness;

    public void setTurn(String turn) {
        _turn = turn;
    }

    public void setBrightness(int brightness) {
        _brightness = brightness;
    }
}

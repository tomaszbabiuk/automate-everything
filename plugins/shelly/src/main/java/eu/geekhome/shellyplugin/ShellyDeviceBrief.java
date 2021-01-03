package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

/*
   "device":{
      "type":"SHPLG-S",
      "mac":"CC50E3376CC2",
      "hostname":"shellyplug-s-376CC2",
      "num_outputs":1,
      "num_meters":1
   }
 */
public class ShellyDeviceBrief {

    @SerializedName("type")
    private String _type;

    @SerializedName("hostname")
    private String _hostname;

    @SerializedName("num_outputs")
    private int _numOutputs;

    @SerializedName("num_meters")
    private int _numMeters;

    public String getType() {
        return _type;
    }

    public int getNumOutputs() {
        return _numOutputs;
    }

    public int getNumMeters() {
        return _numMeters;
    }

    public String getHostname() {
        return _hostname;
    }
}

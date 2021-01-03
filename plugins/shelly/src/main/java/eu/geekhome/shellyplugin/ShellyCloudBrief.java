package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

/*
   "cloud":{
      "enabled":false,
      "connected":false
   },
 */
public class ShellyCloudBrief {
    @SerializedName("enabled")
    private boolean _enabled;

    public boolean isEnabled() {
        return _enabled;
    }
}

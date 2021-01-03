package eu.geekhome.shellyplugin;

import com.google.gson.annotations.SerializedName;

/*
   "meters":[
      {
         "power":0.00,
         "is_valid":true,
         "timestamp":1575191987,
         "counters":[
            0.000,
            0.000,
            0.000
         ],
         "total":0
      }
   ]
 */
public class ShellyMeterBrief {
    @SerializedName("power")
    private double _power;

    public double getPower() {
        return _power;
    }
}

package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class SynchronizedAlert extends SynchronizedNamedObject {

    @SerializedName("raisedOn")
    private Date _raisedOn;

    @SerializedName("devicesIds")
    private ArrayList<String> _devicesIds;

    public SynchronizedAlert() {
    }

    public Date getRaisedOn() {
        return _raisedOn;
    }

    public void setRaisedOn(Date raisedOn) {
        _raisedOn = raisedOn;
    }

    public ArrayList<String> getDevicesIds() {
        return _devicesIds;
    }

    public void setDevicesIds(ArrayList<String> devicesIds) {
        _devicesIds = devicesIds;
    }
}

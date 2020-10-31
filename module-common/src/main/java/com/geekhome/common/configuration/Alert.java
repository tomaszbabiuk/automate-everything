package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.ArrayList;
import java.util.Date;

public class Alert extends NamedObject implements IBlocksTarget {
    private String _alertServicesIds;
    private Date _raisedOn;
    private ArrayList<String> _devicesIds;

    @Persistable(name="AlertServicesIds")
    public String getAlertServicesIds() {
        return _alertServicesIds;
    }

    public void setAlertServicesIds(String value) {
        _alertServicesIds = value;
    }

    public Alert(DescriptiveName name, String alertServicesIds) {
        super(name);
        _alertServicesIds = alertServicesIds;
    }

    public ArrayList<String> getDevicesIds() {
        return _devicesIds;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        return null;
    }

    public Date getRaisedOn() {
        return _raisedOn;
    }

    public void setRaisedOn(Date raisedOn) {
        _raisedOn = raisedOn;
    }

    public void setDevicesIds(ArrayList<String> devicesIds) {
        _devicesIds = devicesIds;
    }
}

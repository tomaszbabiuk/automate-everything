package com.geekhome.common.hardwaremanager;

import com.geekhome.common.configuration.DescriptiveName;

import java.util.ArrayList;

public interface IPort {
    String getId();
    ArrayList<DescriptiveName> getMappedTo();
    void initialize(IPortMapper portMapper);
}

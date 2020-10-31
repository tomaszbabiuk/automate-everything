package com.geekhome.common.hardwaremanager;

import com.geekhome.common.configuration.DescriptiveName;

import java.util.ArrayList;

public interface IPortMapper {
    ArrayList<DescriptiveName> buildPortMappings(String portId);
}

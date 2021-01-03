package com.geekhome.common;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.hardwaremanager.IPortMapper;

import java.util.ArrayList;

public class PortBase implements IPort {
    private String _id;
    private IPortMapper _portMapper;

    @Persistable(name="Id")
    public String getId() {
        return _id;
    }

    private void setId(String value) {
        _id = value;
    }

    public void initialize(IPortMapper portMapper) {
        _portMapper = portMapper;
    }

    public PortBase(String id) {
        setId(id);
    }

    @Persistable(name="MappedTo")
    public ArrayList<DescriptiveName> getMappedTo() {
        if (_portMapper == null) {
            return new ArrayList<>();
        }

        return _portMapper.buildPortMappings(getId());
    }
}
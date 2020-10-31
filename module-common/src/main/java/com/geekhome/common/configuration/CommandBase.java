package com.geekhome.common.configuration;

import com.geekhome.common.NamedObject;

public class CommandBase extends NamedObject {
    public CommandBase(DescriptiveName name) {
        super(name);
    }

    public boolean matches(String subject) {
        return subject!= null && !subject.isEmpty() && getName().getName().toLowerCase().equals(subject.toLowerCase());
    }
}

package eu.geekhome.rest;

import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import eu.geekhome.automation.AutomationConductor;

@Service
public class AutomationConductorHolderService extends HolderService<AutomationConductor> {

    public AutomationConductorHolderService(@Context Application app) throws Exception {
        super(app, AutomationConductor.class);
    }
}
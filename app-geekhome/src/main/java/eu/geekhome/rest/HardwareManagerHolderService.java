package eu.geekhome.rest;

import eu.geekhome.HardwareManager;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Service
public class HardwareManagerHolderService extends HolderService<HardwareManager> {

    public HardwareManagerHolderService(@Context Application app) throws Exception {
        super(app, HardwareManager.class);
    }
}
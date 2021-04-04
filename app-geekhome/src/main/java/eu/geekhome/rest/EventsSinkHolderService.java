package eu.geekhome.rest;

import eu.geekhome.services.events.EventsSink;
import org.jvnet.hk2.annotations.Service;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

@Service
public class EventsSinkHolderService extends HolderService<EventsSink> {

    public EventsSinkHolderService(@Context Application app) {
        super(app, EventsSink.class);
    }
}
package eu.geekhome.rest.hardware;

import eu.geekhome.rest.PluginsCoordinator;
import eu.geekhome.services.hardware.HardwareAdapterFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("hardwarefactories")
public class AdapterFactoryController {

    private PluginsCoordinator _pluginsCoordinator;

    @Inject
    public AdapterFactoryController(PluginsCoordinator pluginsCoordinator) {
        _pluginsCoordinator = pluginsCoordinator;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<String> getFactories() {
        return _pluginsCoordinator
                .getHardwareManagerAdapterFactories()
                .stream()
                .map(HardwareAdapterFactory::getId)
                .collect(Collectors.toList());
    }
}
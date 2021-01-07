package eu.geekhome.rest.hardware;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.hardware.HardwareManagerFactoryDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("hardwarefactories")
public class AdapterController {

    private PluginsManager _pluginsManager;

    @Inject
    public AdapterController(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<HardwareManagerFactoryDto> getFactories() {
        return _pluginsManager
                .getHardwareManagerAdapterFactories()
                .stream()
                .map(factory -> new HardwareManagerFactoryDto(factory.getClass().getName(), factory.getName()))
                .collect(Collectors.toList());
    }
}
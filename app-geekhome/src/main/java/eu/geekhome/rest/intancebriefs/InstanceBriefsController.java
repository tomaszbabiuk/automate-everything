package eu.geekhome.rest.intancebriefs;

import eu.geekhome.rest.PluginsCoordinator;
import eu.geekhome.services.configurable.ConfigurableType;
import eu.geekhome.services.repository.InstanceBriefDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("instancebriefs")
public class InstanceBriefsController {

    private final Repository _repository;
    private final PluginsCoordinator _pluginsCoordinator;

    @Inject
    public InstanceBriefsController(PluginsCoordinator pluginsCoordinator) {
        _pluginsCoordinator = pluginsCoordinator;
        _repository = pluginsCoordinator.getRepositories().get(0);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<InstanceBriefDto> getAllInstancesBriefs() {
        return _repository
                .getAllInstanceBriefs();
    }

    @GET
    @Path("/{configurableType}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<InstanceBriefDto> getAllInstancesBriefsOfConfigurableType(@PathParam("configurableType") ConfigurableType type) {
        List<String> classesOfConfigurableType = _pluginsCoordinator
                .getConfigurables()
                .stream()
//TODO              .filter(configurable -> configurable.getType() == type)
                .map(configurable -> configurable.getClass().getName())
                .collect(Collectors.toList());

        return _repository
                .getAllInstanceBriefs()
                .stream()
                .filter(instanceBriefDto -> classesOfConfigurableType.contains(instanceBriefDto.getClazz()))
                .collect(Collectors.toList());
    }
}
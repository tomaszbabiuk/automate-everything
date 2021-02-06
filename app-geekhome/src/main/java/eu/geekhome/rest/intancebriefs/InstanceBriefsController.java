package eu.geekhome.rest.intancebriefs;

import eu.geekhome.rest.PluginsCoordinator;
import eu.geekhome.services.repository.InstanceBriefDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("instancebriefs")
public class InstanceBriefsController {

    private final Repository _repository;

    @Inject
    public InstanceBriefsController(PluginsCoordinator pluginsCoordinator) {
        _repository = pluginsCoordinator.getRepositories().get(0);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<InstanceBriefDto> getAllInstancesBriefs() {
        return _repository
                .getAllInstanceBriefs();
    }
}
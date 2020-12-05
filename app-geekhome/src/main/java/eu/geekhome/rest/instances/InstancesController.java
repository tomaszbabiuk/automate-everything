package eu.geekhome.rest.instances;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.repository.InstanceDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("instances")
public class InstancesController {

    private PluginsManager _pluginsManager;

    @Inject
    public InstancesController(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<InstanceDto> postInstances(InstanceDto instanceDto) {
        _pluginsManager.getRepositories().forEach(repository ->
                repository.saveInstance(instanceDto));

        return null;
    }

}
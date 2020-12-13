package eu.geekhome.rest.instances;

import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.repository.InstanceDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("instances")
public class InstancesController {

    private final Repository _repository;

    @Inject
    public InstancesController(PluginsManager pluginsManager) {
        _repository = pluginsManager.getRepositories().get(0);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void postInstances(InstanceDto instanceDto) {
        _repository.saveInstance(instanceDto);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public InstanceDto getInstancesById(@PathParam("id") long id) {
        return _repository.getInstance(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<InstanceDto> getInstancesOfClass(@QueryParam("class") String clazz) {
        if (clazz != null) {
            return _repository.getInstancesOfClazz(clazz);
        }

        return _repository.getAllInstances();
    }
}
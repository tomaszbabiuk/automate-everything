package eu.geekhome.rest.instances;

import eu.geekhome.services.configurable.Configurable;
import eu.geekhome.services.configurable.FieldDefinition;
import eu.geekhome.services.configurable.FieldValidationResult;
import eu.geekhome.rest.PluginsCoordinator;
import eu.geekhome.services.repository.InstanceDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("instances")
public class InstancesController {

    private Repository _repository;
    private final PluginsCoordinator _pluginsCoordinator;

    private Optional<Configurable> findConfigurable(String clazz) {
        return _pluginsCoordinator
                .getConfigurables()
                .stream()
                .filter((x) -> x.getClass().getSimpleName().equals(clazz))
                .findFirst();
    }

    @Inject
    public InstancesController(PluginsCoordinator pluginsCoordinator) {
        _pluginsCoordinator = pluginsCoordinator;
        try {
            _repository = pluginsCoordinator.getRepositories().get(0);
        } catch (Exception ex) {
            _repository = pluginsCoordinator.getRepositories().get(0);
        }
    }

    @SuppressWarnings("rawtypes")
    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Map<String, FieldValidationResult> postInstances(InstanceDto instanceDto) throws Exception {
        Optional<Configurable> configurable = findConfigurable(instanceDto.getClazz());
        if (configurable.isPresent()) {
            Map<String, FieldValidationResult> validationResult = new HashMap<>();
            boolean isObjectValid = true;

            for (FieldDefinition fieldDefinition : configurable.get().getFieldDefinitions()) {
                String fieldValue = instanceDto.getFields().get(fieldDefinition.getName());
                FieldValidationResult isValid = fieldDefinition.validate(fieldValue);
                validationResult.put(fieldDefinition.getName(), isValid);
                if (!isValid.isValid()) {
                    isObjectValid = false;
                }
            }

            if (isObjectValid) {
                _repository.saveInstance(instanceDto);
            }

            return validationResult;
        } else {
            throw new Exception("Unsupported configurable class");
        }
    }

    @SuppressWarnings("rawtypes")
    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Map<String, FieldValidationResult> putInstances(InstanceDto instanceDto) throws Exception {
        Optional<Configurable> configurable = findConfigurable(instanceDto.getClazz());
        if (configurable.isPresent()) {
            Map<String, FieldValidationResult> validationResult = new HashMap<>();
            boolean isObjectValid = true;

            for (FieldDefinition fieldDefinition : configurable.get().getFieldDefinitions()) {
                String fieldValue = instanceDto.getFields().get(fieldDefinition.getName());
                FieldValidationResult isValid = fieldDefinition.validate(fieldValue);
                validationResult.put(fieldDefinition.getName(), isValid);
                if (!isValid.isValid()) {
                    isObjectValid = false;
                }
            }

            if (isObjectValid) {
                _repository.updateInstance(instanceDto);
            }

            return validationResult;
        } else {
            throw new Exception("Unsupported configurable class");
        }
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

        return new ArrayList<>();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void deleteInstance(@PathParam("id") long id) {
        _repository.deleteInstance(id);
    }
}
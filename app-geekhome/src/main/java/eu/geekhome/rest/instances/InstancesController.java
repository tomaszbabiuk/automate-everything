package eu.geekhome.rest.instances;

import com.geekhome.common.configurable.Configurable;
import com.geekhome.common.configurable.FieldDefinition;
import com.geekhome.common.configurable.FieldValidationResult;
import eu.geekhome.rest.PluginsManager;
import eu.geekhome.services.repository.InstanceDto;
import eu.geekhome.services.repository.Repository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("instances")
public class InstancesController {

    private Repository _repository;
    private final PluginsManager _pluginsManager;

    private Optional<Configurable> findConfigurable(String clazz) {
        return _pluginsManager
                .getConfigurables()
                .stream()
                .filter((x) -> x.getClass().getSimpleName().equals(clazz))
                .findFirst();
    }

    @Inject
    public InstancesController(PluginsManager pluginsManager) {
        _pluginsManager = pluginsManager;
        try {
            _repository = pluginsManager.getRepositories().get(0);
        } catch (Exception ex) {
            _repository = pluginsManager.getRepositories().get(0);
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
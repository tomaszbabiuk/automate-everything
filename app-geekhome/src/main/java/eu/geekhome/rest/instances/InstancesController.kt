package eu.geekhome.rest.instances

import eu.geekhome.data.Repository
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.rest.settings.ValidationResultMap
import eu.geekhome.domain.configurable.ConfigurableWithFields
import eu.geekhome.domain.configurable.FieldValidationResult
import java.util.*
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("instances")
class InstancesController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository
) {

    private fun findConfigurable(clazz: String): ConfigurableWithFields? {
        return pluginsCoordinator
            .configurables
            .filter { x -> x.javaClass.name.equals(clazz) }
            .filterIsInstance<ConfigurableWithFields>()
            .firstOrNull()
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun postInstances(instanceDto: InstanceDto): ValidationResultMap {
        return validate(instanceDto) {
            repository.saveInstance(instanceDto)
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun putInstances(instanceDto: InstanceDto): ValidationResultMap {
        return validate(instanceDto) {
            repository.updateInstance(instanceDto)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstancesById(@PathParam("id") id: Long): InstanceDto {
        return repository.getInstance(id)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstancesOfClass(@QueryParam("class") clazz: String?): List<InstanceDto> {
        return if (clazz != null) {
            repository.getInstancesOfClazz(clazz)
        } else ArrayList()
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteInstance(@PathParam("id") id: Long) {
        repository.deleteInstance(id)
    }

    private fun validate(instanceDto: InstanceDto, onValidCallback: () -> Unit):
            MutableMap<String, FieldValidationResult> {
        val configurable = findConfigurable(instanceDto.clazz)
        return if (configurable != null) {
            val validationResult: MutableMap<String, FieldValidationResult> = HashMap()
            var isObjectValid = true
            for (fieldDefinition in configurable.fieldDefinitions.values) {
                val fieldValue = instanceDto.fields[fieldDefinition.name]
                val isValid = fieldDefinition.validate(fieldValue)
                validationResult[fieldDefinition.name] = isValid
                if (!isValid.valid) {
                    isObjectValid = false
                }
            }
            if (isObjectValid) {
                onValidCallback()
            }
            validationResult
        } else {
            throw Exception("Unsupported configurable class")
        }
    }
}

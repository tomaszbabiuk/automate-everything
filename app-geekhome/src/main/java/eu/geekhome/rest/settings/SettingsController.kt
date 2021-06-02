package eu.geekhome.rest.settings

import eu.geekhome.PluginsCoordinator
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.services.configurable.FieldValidationResult
import eu.geekhome.services.configurable.SettingGroup
import eu.geekhome.services.repository.InstanceDto
import eu.geekhome.services.repository.SettingsDto
import java.util.*
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("settings")
class SettingsController @Inject constructor(pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {

    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    private fun findSettingCategory(clazz: String): SettingGroup? {
        return pluginsCoordinator
            .settingGroups
            .firstOrNull { x -> x.javaClass.name.equals(clazz) }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun postInstances(settingsDto: SettingsDto): Map<String, FieldValidationResult> {
        return validate(settingsDto) {
            pluginsCoordinator.repository.saveSettings(settingsDto)
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun putInstances(settingsDto: SettingsDto): Map<String, FieldValidationResult> {
        return validate(settingsDto) {
            pluginsCoordinator.repository.updateSettings(settingsDto)
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstancesById(@PathParam("id") id: Long): InstanceDto {
        return pluginsCoordinator.repository.getInstance(id)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getInstancesOfClass(@QueryParam("class") clazz: String?): List<InstanceDto> {
        return if (clazz != null) {
            pluginsCoordinator.repository.getInstancesOfClazz(clazz)
        } else ArrayList()
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun deleteInstance(@PathParam("id") id: Long) {
        pluginsCoordinator.repository.deleteInstance(id)
    }

    private fun validate(settingsDto: SettingsDto, onValidCallback: () -> (Unit)):
            Map<String, FieldValidationResult> {
        val category = findSettingCategory(settingsDto.clazz)
        return if (category != null) {
            val validationResult: MutableMap<String, FieldValidationResult> = HashMap()
            var isObjectValid = true
            for (fieldDefinition in category.fieldDefinitions.values) {
                val fieldValue = settingsDto.fields[fieldDefinition.name]
                val isValid = fieldDefinition.validate(fieldValue)
                validationResult[fieldDefinition.name] = isValid
                if (!isValid.isValid) {
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

package eu.geekhome.rest.settings

import eu.geekhome.PluginsCoordinator
import eu.geekhome.rest.PluginsCoordinatorHolderService
import eu.geekhome.services.configurable.FieldValidationResult
import eu.geekhome.services.configurable.SettingGroup
import eu.geekhome.services.extensibility.PluginMetadata
import eu.geekhome.services.repository.SettingsDto
import java.util.*
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import kotlin.collections.HashMap

typealias ValidationResultMap = Map<String, FieldValidationResult>
typealias SettingsValuesMap = Map<String, String?>

@Path("settings")
class SettingsController @Inject constructor(pluginsCoordinatorHolderService: PluginsCoordinatorHolderService) {

    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

    private fun findSettingCategory(clazz: String): SettingGroup? {
        return pluginsCoordinator
            .plugins
            .map {it.plugin }
            .filterIsInstance<PluginMetadata>()
            .flatMap { it.settingGroups }
            .firstOrNull() { it.javaClass.name == clazz }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getSettings(@Context request: HttpServletRequest?): Map<String, SettingsValuesMap> {
        val result = HashMap<String, SettingsValuesMap>()
        pluginsCoordinator
            .repository
            .getAllSettings()
            .forEach {
                result[it.clazz] = it.fields
            }

        return result
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun putSettings(settings: Map<String, SettingsValuesMap>): Map<String, ValidationResultMap> {
        val validationResult = HashMap<String, ValidationResultMap>()
        var hasErrors = false

        val settingsDtos = settings.map { SettingsDto(it.key, it.value) }
        settingsDtos.forEach { settingsDto ->
            val validation = validate(settingsDto)
            validation.entries.forEach {
                if (!it.value.isValid) {
                    hasErrors = true
                }
            }

            validationResult[settingsDto.clazz] = validation
        }

        if (!hasErrors) {
            pluginsCoordinator.repository.updateSettings(settingsDtos)
        }

        return validationResult
    }

    private fun validate(settingsDto: SettingsDto):
            ValidationResultMap {
        val category = findSettingCategory(settingsDto.clazz)
        return if (category != null) {
            val validationResult: MutableMap<String, FieldValidationResult> = HashMap()
            for (fieldDefinition in category.fieldDefinitions.values) {
                val fieldValue = settingsDto.fields[fieldDefinition.name]
                val isValid = fieldDefinition.validate(fieldValue)
                validationResult[fieldDefinition.name] = isValid
            }
            validationResult
        } else {
            throw Exception("Unsupported settings class")
        }
    }
}

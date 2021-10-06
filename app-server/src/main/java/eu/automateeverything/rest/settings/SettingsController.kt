package eu.automateeverything.rest.settings

import eu.automateeverything.data.Repository
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.configurable.FieldValidationResult
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.extensibility.PluginMetadata
import jakarta.servlet.http.HttpServletRequest
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import kotlin.collections.HashMap

typealias ValidationResultMap = Map<String, FieldValidationResult>
typealias SettingsValuesMap = Map<String, String?>

@Path("settings")
class SettingsController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository
) {

    private fun findSettingCategory(clazz: String): SettingGroup? {
        return pluginsCoordinator
            .plugins
            .map {it.plugin }
            .filterIsInstance<PluginMetadata>()
            .flatMap { it.settingGroups }
            .firstOrNull { it.javaClass.name == clazz }
    }

    @GET
    @Path("/{pluginId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getSettings(@PathParam("pluginId") pluginId: String): Map<String, SettingsValuesMap> {
        val result = HashMap<String, SettingsValuesMap>()
        repository
            .getSettingsByPluginId(pluginId)
            .forEach {
                result[it.clazz] = it.fields
            }

        return result
    }

    @PUT
    @Path("/{pluginId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Throws(Exception::class)
    fun putSettings(@PathParam("pluginId") pluginId: String, settings: Map<String, SettingsValuesMap>): Map<String, ValidationResultMap> {
        val validationResult = HashMap<String, ValidationResultMap>()
        var hasErrors = false

        val settingsDtos = settings.map { SettingsDto(pluginId, it.key, it.value) }
        settingsDtos.forEach { settingsDto ->
            val validation = validate(settingsDto)
            validation.entries.forEach {
                if (!it.value.valid) {
                    hasErrors = true
                }
            }

            validationResult[settingsDto.clazz] = validation
        }

        if (!hasErrors) {
            repository.updateSettings(settingsDtos)
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
                val isValid = fieldDefinition.validate(fieldValue, settingsDto.fields)
                validationResult[fieldDefinition.name] = isValid
            }
            validationResult
        } else {
            throw Exception("Unsupported settings class")
        }
    }
}

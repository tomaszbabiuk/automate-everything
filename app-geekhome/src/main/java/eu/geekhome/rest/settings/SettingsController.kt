package eu.geekhome.rest.settings

import eu.geekhome.PluginsCoordinator
import eu.geekhome.domain.configurable.FieldValidationResult
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.repository.Repository
import eu.geekhome.domain.repository.SettingsDto
import javax.inject.Inject
import javax.servlet.http.HttpServletRequest
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import kotlin.collections.HashMap

typealias ValidationResultMap = Map<String, FieldValidationResult>
typealias SettingsValuesMap = Map<String, String?>

@Path("settings")
class SettingsController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository) {

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
    fun getSettings(@PathParam("pluginId") pluginId: String, @Context request: HttpServletRequest?): Map<String, SettingsValuesMap> {
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
                if (!it.value.isValid) {
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
                val isValid = fieldDefinition.validate(fieldValue)
                validationResult[fieldDefinition.name] = isValid
            }
            validationResult
        } else {
            throw Exception("Unsupported settings class")
        }
    }
}

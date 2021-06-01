package eu.geekhome.rest.settingsgroup

import eu.geekhome.PluginsCoordinator
import javax.inject.Inject
import eu.geekhome.rest.PluginsCoordinatorHolderService
import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.servlet.http.HttpServletRequest
import eu.geekhome.services.configurable.SettingsCategoryDto
import java.util.stream.Collectors
import javax.ws.rs.Path
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType

@Path("settingcategories")
class SettingCategoryController @Inject constructor(
    pluginsCoordinatorHolderService: PluginsCoordinatorHolderService,
    private val settingGroupCategoryDtoMapper: SettingCategoryDtoMapper
) {
    private val pluginsCoordinator: PluginsCoordinator = pluginsCoordinatorHolderService.instance

//    @GET
//    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    fun getSettingsGroups(@Context request: HttpServletRequest?): List<SettingsCategoryDto> {
//        return pluginsCoordinator
//            .settingCategories
//            .stream()
//            .map { settingGroupCategoryDtoMapper.map(it) }
//            .collect(Collectors.toList())
//    }
}
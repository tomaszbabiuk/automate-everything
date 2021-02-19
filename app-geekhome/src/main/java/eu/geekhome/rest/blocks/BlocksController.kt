package eu.geekhome.rest.blocks

import eu.geekhome.rest.PluginsCoordinator
import eu.geekhome.rest.ResourceNotFoundException
import eu.geekhome.services.automation.StateType
import eu.geekhome.services.configurable.StateDeviceConfigurable
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("blocks")
class BlocksController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator
) {
    @Throws(ResourceNotFoundException::class)
    @GET
    @Path("/{configurableClazz}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getToolbox(@PathParam("configurableClazz") configurableClazz : String): BlocklyToolboxWithBlocksDto {
        try {
            val configurable = pluginsCoordinator.configurables.first {
                it.javaClass.name.equals(configurableClazz)
            }

            val blockImplementations = ArrayList<Any>()
            val thisDeviceBlockDefinitions = ArrayList<BLocklyToolboxItemBlockDto>()

            //building "this device" blocks
            if (configurable is StateDeviceConfigurable) {
                configurable
                    .states
                    .filter { it.value.type != StateType.ReadOnly }
                    .forEach {
                        val type = "change_state_${it.value.name.id}"
                        val blockDef = BLocklyToolboxItemBlockDto(type=type)
                        thisDeviceBlockDefinitions.add(blockDef)

                        val blockImpl = ChangeStateBlockDto(type=type, message0 = it.value.name )
                        blockImplementations.add(blockImpl)
                     }
            }

            val toolbox = BlocklyToolboxDto(
                contents = listOf(
                    BlocklyToolboxItemCategoryDto(name = R.category_name_this_device, contents = thisDeviceBlockDefinitions)
                )
            )

            return BlocklyToolboxWithBlocksDto(toolbox, blockImplementations)
        } catch (ex: NoSuchElementException) {
            throw ResourceNotFoundException()
        }
    }
}


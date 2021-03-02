package eu.geekhome.rest.blocks

import eu.geekhome.automation.blocks.BlockFactoriesCollector
import eu.geekhome.automation.blocks.IBlockFactoriesCollector
import eu.geekhome.rest.*
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("blocks")
class BlocksController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    blockFactoriesCollectorHolder: BlockFactoriesCollectorHolderService,
) {
    val blockFactoriesCollector : IBlockFactoriesCollector = blockFactoriesCollectorHolder.instance

    @Throws(ResourceNotFoundException::class)
    @GET
    @Path("/{configurableClazz}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getToolbox(@PathParam("configurableClazz") configurableClazz : String): BlocklyToolboxWithBlocksDto {
        try {
            val configurable = pluginsCoordinator.configurables.first {
                it.javaClass.name.equals(configurableClazz)
            }

            val blockFactories = blockFactoriesCollector.collect(configurable)
            val blockCategories = ArrayList<BlocklyToolboxItemCategoryDto>()
            val blockImplementations = ArrayList<RawJson>()

            blockFactories.forEach { blockFactory ->
                var category = blockCategories.find { it.name == blockFactory.category}
                if (category == null) {
                    category = BlocklyToolboxItemCategoryDto(blockFactory.category, ToolboxItemKind.category, ArrayList())
                    blockCategories.add(category)
                }
                category.contents.add(BLocklyToolboxItemBlockDto(ToolboxItemKind.block, blockFactory.type))
                blockImplementations.add(blockFactory.buildBlock())
            }

            val toolbox = BlocklyToolboxDto(
                contents = blockCategories
            )

            return BlocklyToolboxWithBlocksDto(toolbox, blockImplementations)
        } catch (ex: NoSuchElementException) {
            throw ResourceNotFoundException()
        }
    }
}


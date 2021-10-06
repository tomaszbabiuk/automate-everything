package eu.automateeverything.rest.blocks

import eu.automateeverything.data.blocks.*
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.rest.*
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("blocks")
class BlocksController @Inject constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val blockFactoriesCollector: BlockFactoriesCollector
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

            val blockFactories = blockFactoriesCollector.collect(configurable)
            val blockCategories = ArrayList<BlocklyToolboxItemCategoryDto>()
            val blockImplementations = ArrayList<RawJson>()

            blockFactories.forEach { blockFactory ->
                var category = blockCategories.find { it.name == blockFactory.category.categoryName}
                if (category == null) {
                    category = BlocklyToolboxItemCategoryDto(blockFactory.category.categoryName, ToolboxItemKind.category, ArrayList())
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


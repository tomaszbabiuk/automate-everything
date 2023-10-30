/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.rest.compositionblocks

import eu.automateeverything.data.blocks.*
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.ResourceNotFoundException
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.automation.blocks.CollectionContext
import eu.automateeverything.domain.extensibility.PluginsCoordinator
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("compositionblocks")
class CompositionBlocksController
@Inject
constructor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val blockFactoriesCollector: BlockFactoriesCollector
) {

    @Throws(ResourceNotFoundException::class)
    @GET
    @Path("/{configurableClazz}/{instanceId}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    fun getToolbox(
        @PathParam("configurableClazz") configurableClazz: String,
        @PathParam("instanceId") instanceId: String
    ): BlocklyToolboxWithBlocksDto {
        try {
            val configurable =
                pluginsCoordinator.configurables.first {
                    it.javaClass.name.equals(configurableClazz)
                }

            val blockFactories =
                blockFactoriesCollector.collect(
                    configurable,
                    instanceId.toLongOrNull(),
                    CollectionContext.Composition
                )
            val blockCategories = ArrayList<BlocklyToolboxItemCategoryDto>()
            val blockImplementations = ArrayList<RawJson>()

            blockFactories.forEach { blockFactory ->
                var category =
                    blockCategories.find { it.name == blockFactory.category.categoryName }
                if (category == null) {
                    category =
                        BlocklyToolboxItemCategoryDto(
                            blockFactory.category.categoryName,
                            ToolboxItemKind.category,
                            ArrayList()
                        )
                    blockCategories.add(category)
                }
                category.contents.add(
                    BLocklyToolboxItemBlockDto(ToolboxItemKind.block, blockFactory.type)
                )
                blockImplementations.add(blockFactory.buildBlock())
            }

            val toolbox = BlocklyToolboxDto(contents = blockCategories)

            return BlocklyToolboxWithBlocksDto(toolbox, blockImplementations)
        } catch (ex: NoSuchElementException) {
            throw ResourceNotFoundException()
        }
    }
}

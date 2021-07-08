package eu.geekhome.rest.blocks

import eu.geekhome.domain.automation.RawJson

data class BlocklyToolboxDto(
    val kind: ToolboxKind = ToolboxKind.categoryToolbox,
    val contents: List<BlocklyToolboxItemCategoryDto>
)

data class BlocklyToolboxWithBlocksDto(
    val toolbox: BlocklyToolboxDto,
    val blocks: List<RawJson>
)
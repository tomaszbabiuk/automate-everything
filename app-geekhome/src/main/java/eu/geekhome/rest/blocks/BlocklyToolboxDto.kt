package eu.geekhome.rest.blocks

data class BlocklyToolboxDto(
    val kind: ToolboxKind = ToolboxKind.categoryToolbox,
    val contents: List<BlocklyToolboxItemCategoryDto>
)

data class BlocklyToolboxWithBlocksDto(
    val toolbox: BlocklyToolboxDto,
    val blocks: List<Any>
)
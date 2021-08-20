package eu.geekhome.data.blocks

data class BlocklyToolboxDto(
    val kind: ToolboxKind = ToolboxKind.categoryToolbox,
    val contents: List<BlocklyToolboxItemCategoryDto>
)

data class BlocklyToolboxWithBlocksDto(
    val toolbox: BlocklyToolboxDto,
    val blocks: List<RawJson>
)
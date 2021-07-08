package eu.geekhome.data.blocks

data class BLocklyToolboxItemBlockDto(
    val kind: ToolboxItemKind = ToolboxItemKind.block,
    val type: String
)
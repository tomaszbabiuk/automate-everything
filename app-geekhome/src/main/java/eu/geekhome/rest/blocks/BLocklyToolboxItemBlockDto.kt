package eu.geekhome.rest.blocks

data class BLocklyToolboxItemBlockDto(
    val kind: ToolboxItemKind = ToolboxItemKind.block,
    val type: String
)
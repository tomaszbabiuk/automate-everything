package eu.automateeverything.data.blocks

data class BLocklyToolboxItemBlockDto(
    val kind: ToolboxItemKind = ToolboxItemKind.block,
    val type: String
)
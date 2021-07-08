package eu.geekhome.data.blocks

import eu.geekhome.data.localization.Resource

data class BlocklyToolboxItemCategoryDto(
    val name: Resource,
    val kind: ToolboxItemKind = ToolboxItemKind.category,
    val contents: MutableList<BLocklyToolboxItemBlockDto>
)
package eu.automateeverything.data.blocks

import eu.automateeverything.data.localization.Resource

data class BlocklyToolboxItemCategoryDto(
    val name: Resource,
    val kind: ToolboxItemKind = ToolboxItemKind.category,
    val contents: MutableList<BLocklyToolboxItemBlockDto>
)
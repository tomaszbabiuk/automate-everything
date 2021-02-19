package eu.geekhome.rest.blocks

import eu.geekhome.services.localization.Resource

data class BlocklyToolboxItemCategoryDto(
    val name: Resource,
    val kind: ToolboxItemKind = ToolboxItemKind.category,
    val contents: List<BLocklyToolboxItemBlockDto>
)
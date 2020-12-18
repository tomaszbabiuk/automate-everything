package eu.geekhome.services.repository

import org.pf4j.ExtensionPoint

interface Repository : ExtensionPoint {

    fun saveInstance(instanceDto: InstanceDto)
    fun getAllInstances(): List<InstanceDto>
    fun getInstancesOfClazz(clazz: String): List<InstanceDto>
    fun getInstance(id: Long): InstanceDto

    fun getAllTags(): List<TagDto>
    fun saveTag(tag: TagDto): Long
    fun deleteTag(id: Long)
    fun updateTag(tagDto: TagDto)

    fun getAllIconCategories(): List<IconCategoryDto>
    fun saveIconCategory(iconCategoryDto: IconCategoryDto) : Long
    fun deleteIconCategory(id: Long)
    fun updateIconCategory(iconCategoryDto: IconCategoryDto)

    fun getAllIcons(): List<IconDto>
    fun saveIcon(iconDto: IconDto) : Long
    fun deleteIcon(id: Long)
    fun updateIcon(iconDto: IconDto)
}
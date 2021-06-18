package eu.geekhome.domain.repository

import eu.geekhome.domain.hardware.PortDto

interface Repository {

    fun saveInstance(instanceDto: InstanceDto)
    fun updateInstance(instanceDto: InstanceDto)
    fun getAllInstances(): List<InstanceDto>
    fun getAllInstanceBriefs() : List<InstanceBriefDto>
    fun getInstancesOfClazz(clazz: String): List<InstanceDto>
    fun deleteInstance(id: Long)
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
    fun getIcon(id: Long): IconDto
    fun saveIcon(iconDto: IconDto) : Long
    fun deleteIcon(id: Long)
    fun updateIcon(iconDto: IconDto)

    fun getAllPorts(): List<PortDto>
    fun savePort(port: PortDto): Long
    fun deletePortSnapshot(id: String)

    fun updateSettings(settingsDtos: List<SettingsDto>)
    fun getAllSettings() : List<SettingsDto>
    fun getSettingsByClazz(clazz: String): SettingsDto?
}
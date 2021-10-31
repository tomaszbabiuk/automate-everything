package eu.automateeverything.data

import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.data.icons.IconCategoryDto
import eu.automateeverything.data.icons.IconDto
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceBriefDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.data.tags.TagDto

interface Repository {

    fun saveInstance(instanceDto: InstanceDto)
    fun updateInstance(instanceDto: InstanceDto)
    fun getAllInstances(): List<InstanceDto>
    fun getAllInstanceBriefs() : List<InstanceBriefDto>
    fun getInstancesOfClazz(clazz: String): List<InstanceDto>
    fun deleteInstance(id: Long)
    fun getInstance(id: Long): InstanceDto
    fun clearInstanceUpdatedFlag()
    fun hasUpdatedInstance(): Boolean

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
    fun getPortById(id: String): PortDto?
    fun updatePort(port: PortDto): Long

    fun deletePort(id: String)
    fun deletePortSnapshot(id: String)
    fun updateSettings(settingsDtos: List<SettingsDto>)
    fun getSettingsByPluginIdAndClazz(pluginId: String, clazz: String): SettingsDto?

    fun getSettingsByPluginId(pluginId: String): List<SettingsDto>
    fun getInboxItems(limit: Long, offset: Long): List<InboxItemDto>
    fun getUnreadInboxItems() : List<InboxItemDto>
    fun saveInboxItem(message: InboxItemDto) : Long
    fun markInboxItemAsRead(id: Long) : InboxItemDto
    fun deleteInboxItem(id: Long)
    fun countInboxItems(): Long
}
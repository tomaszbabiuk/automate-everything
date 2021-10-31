package eu.automateeverything.sqldelightplugin

import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.data.icons.IconCategoryDto
import eu.automateeverything.data.icons.IconDto
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceBriefDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.data.tags.TagDto
import eu.automateeverything.sqldelightplugin.database.*

interface Mapper<From, To> {
    fun map(from: From) : To
}

class PortSnapshotMapper : Mapper<PortSnapshot, PortDto> {
    override fun map(from: PortSnapshot): PortDto {
        return PortDto(
            from.id,
            from.factoryId,
            from.adapterId,
            null,
            null,
            from.valueClazz,
            from.canRead == 1L,
            from.canWrite == 1L,
            false
        )
    }
}

class IconToIconDtoMapper: Mapper<Icon, IconDto> {
    override fun map(from: Icon): IconDto {
        return IconDto(from.id, from.icon_category_id, from.raw)
    }
}

class SelectAllWithIconsToIconCategoryDtoMapper: Mapper<SelectAllWithIcons, IconCategoryDto> {
    override fun map(from: SelectAllWithIcons): IconCategoryDto {
        val iconIds = convertStringOfIdsToList(from.iconIds)
        return IconCategoryDto(from.id, from.name, iconIds)
    }
}

class SelectAllShortToInstanceBriefDtoMapper: Mapper<SelectAllShort, InstanceBriefDto> {
    override fun map(from: SelectAllShort): InstanceBriefDto {
        return InstanceBriefDto(from.id, from.clazz, from.value)
    }
}

class ConfigurableInstanceWithTagIdsToInstanceDtoMapper(val database: Database): Mapper<ConfigurableInstanceWithTagIds, InstanceDto> {
    override fun map(from: ConfigurableInstanceWithTagIds): InstanceDto {
        val fieldsMap = HashMap<String, String?>()

        database
            .configurableFieldInstanceQueries
            .selectOfConfigurableInstance(from.id)
            .executeAsList()
            .forEach { fieldsMap[it.name] = it.value }

        return InstanceDto(
            from.id,
            from.icon_id,
            convertStringOfIdsToList(from.tagIds),
            from.clazz,
            fieldsMap,
            from.automation
        )
    }
}

class TagToTagDtoMapper : Mapper<Tag, TagDto> {
    override fun map(from: Tag): TagDto {
        return TagDto(from.id, from.parent_id, from.name)
    }
}

class SettingsFieldInstanceListToSettingsDtoListMapper : Mapper<List<SettingsFieldInstance>, List<SettingsDto>> {
    override fun map(from: List<SettingsFieldInstance>): List<SettingsDto> {
        return from
            .groupBy { Pair(it.pluginId, it.clazz) }
            .map {
                val fields = HashMap<String, String?>()
                it.value.forEach { fieldInstance ->
                    fields[fieldInstance.name] = fieldInstance.value
                }

                SettingsDto(it.key.first, it.key.second, fields)
            }
    }
}

class InboxItemToInboxDtoMapper : Mapper<InboxItem, InboxItemDto> {
    override fun map(from: InboxItem): InboxItemDto {
        return InboxItemDto(
            from.id,
            from.message,
            from.timestamp,
            from.kind,
            from.port_id,
            from.read == 1L)
    }
}


private fun convertStringOfIdsToList(input: String): List<Long> {
    return if (input.isNotEmpty()) {
        input.split(',').map { it.toLong() }
    } else {
        ArrayList()
    }
}
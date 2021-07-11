package eu.geekhome.sqldelightplugin

import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.geekhome.data.Repository
import eu.geekhome.data.hardware.PortDto
import eu.geekhome.data.icons.IconCategoryDto
import eu.geekhome.data.icons.IconDto
import eu.geekhome.data.inbox.InboxItemDto
import eu.geekhome.data.instances.InstanceBriefDto
import eu.geekhome.data.instances.InstanceDto
import eu.geekhome.data.settings.SettingsDto
import eu.geekhome.data.tags.TagDto
import eu.geekhome.sqldelightplugin.database.*

class SqlDelightRepository : Repository {

    private var database: Database
    private var hasUpdatedInstance = false

    init {
        Class.forName("org.sqlite.JDBC")
        val driver = JdbcSqliteDriver("jdbc:sqlite:repository.sqlite")
        Database.Schema.create(driver)
        driver.execute(null, "PRAGMA foreign_keys=ON", 0)
        database = Database(
            driver,
            inboxItemAdapter = InboxItem.Adapter(
                kindAdapter = EnumColumnAdapter()
            )
        )
    }

    private val portSnapshotToPortDtoMapper: Mapper<PortSnapshot, PortDto> =
        PortSnapshotMapper()

    private val iconToIconDtoMapper: Mapper<Icon, IconDto> =
        IconToIconDtoMapper()

    private val selectAllWithIconsToIconCategoryDtoMapper: Mapper<SelectAllWithIcons, IconCategoryDto> =
        SelectAllWithIconsToIconCategoryDtoMapper()

    private val selectAllShortToInstanceBriefDtoMapper: Mapper<SelectAllShort, InstanceBriefDto> =
        SelectAllShortToInstanceBriefDtoMapper()

    private val configurableInstanceWithTagIdsToInstanceDtoMapper: Mapper<ConfigurableInstanceWithTagIds, InstanceDto> =
        ConfigurableInstanceWithTagIdsToInstanceDtoMapper(database)

    private val tagToTagDtoMapper : Mapper<Tag, TagDto> =
        TagToTagDtoMapper()

    private val inboxItemToInboxItemDtoMapper : Mapper<InboxItem, InboxItemDto> =
        InboxItemToInboxDtoMapper()

    private val settingsFieldInstanceListToSettingsDtoList:  Mapper<List<SettingsFieldInstance>, List<SettingsDto>> =
        SettingsFieldInstanceListToSettingsDtoListMapper()

    override fun saveInstance(instanceDto: InstanceDto) {
        database.transaction {
            database.configurableInstanceQueries.insert(instanceDto.clazz, instanceDto.iconId, instanceDto.automation)
            val insertedDtoId = database.generalQueries.lastInsertRowId().executeAsOne()
            instanceDto.fields.forEach {
                database.configurableFieldInstanceQueries.insert(insertedDtoId, it.key, it.value)
            }

            instanceDto.tagIds.forEach {
                database.instanceTaggingQueries.insert(it, insertedDtoId)
            }
        }
    }

    override fun updateInstance(instanceDto: InstanceDto) {
        database.transaction {
            database.configurableInstanceQueries.update(instanceDto.clazz, instanceDto.iconId,
                instanceDto.automation, instanceDto.id)

            instanceDto.fields.forEach {
                database.configurableFieldInstanceQueries.update(it.value, it.key, instanceDto.id)
            }

            database.instanceTaggingQueries.deleteAllOfInstance(instanceDto.id)
            instanceDto.tagIds.forEach {
                database.instanceTaggingQueries.insert(it, instanceDto.id)
            }

            hasUpdatedInstance = true
        }
    }

    override fun getAllInstances(): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectAll()
                .executeAsList()
                .map(configurableInstanceWithTagIdsToInstanceDtoMapper::map)
    }

    override fun getAllInstanceBriefs(): List<InstanceBriefDto> {
        return database
                .configurableInstanceQueries
                .selectAllShort()
                .executeAsList()
                .map(selectAllShortToInstanceBriefDtoMapper::map)
    }

    override fun getInstancesOfClazz(clazz: String): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectByClazz(clazz)
                .executeAsList()
                .map(configurableInstanceWithTagIdsToInstanceDtoMapper::map)
    }

    override fun deleteInstance(id: Long) {
        database.transaction {
            database.configurableInstanceQueries.delete(id)
        }
    }

    override fun getInstance(id: Long): InstanceDto {
        val instance = database
                .configurableInstanceQueries
                .selectById(id)
                .executeAsOne()

        return configurableInstanceWithTagIdsToInstanceDtoMapper.map(instance)
    }

    override fun clearInstanceUpdatedFlag() {
        hasUpdatedInstance = false
    }

    override fun hasUpdatedInstance(): Boolean {
        return hasUpdatedInstance
    }

    override fun getAllTags(): List<TagDto> {
        return database
                .tagQueries
                .selectAll()
                .executeAsList()
                .map(tagToTagDtoMapper::map)
    }

    override fun saveTag(tag: TagDto): Long {
        var id: Long = 0
        database.transaction {
            database.tagQueries.insert(tag.parentId, tag.name)
            id = database.generalQueries.lastInsertRowId().executeAsOne()
        }

        return id
    }

    override fun deleteTag(id: Long) {
        database.transaction {
            database.tagQueries.delete(id)
        }
    }

    override fun updateTag(tagDto: TagDto) {
        database.transaction {
            database.tagQueries.update(tagDto.parentId, tagDto.name, tagDto.id)
        }
    }

    override fun getAllIconCategories(): List<IconCategoryDto> {
        return database
                .iconCategoryQueries
                .selectAllWithIcons()
                .executeAsList()
                .map(selectAllWithIconsToIconCategoryDtoMapper::map)
    }

    override fun saveIconCategory(iconCategoryDto: IconCategoryDto): Long {
        var id: Long = 0
        database.transaction {
            database.iconCategoryQueries.insert(iconCategoryDto.name)
            id = database.generalQueries.lastInsertRowId().executeAsOne()
        }

        return id
    }

    override fun deleteIconCategory(id: Long) {
        database.transaction {
            database.iconCategoryQueries.delete(id)
        }
    }

    override fun updateIconCategory(iconCategoryDto: IconCategoryDto) {
        database.transaction {
            database.iconCategoryQueries.update(iconCategoryDto.name, iconCategoryDto.id)
        }
    }

    override fun getAllIcons(): List<IconDto> {
        return database
                .iconQueries
                .selectAll()
                .executeAsList()
                .map(iconToIconDtoMapper::map)
    }

    override fun getIcon(id: Long): IconDto {
        return iconToIconDtoMapper.map(database
                    .iconQueries
                    .selectById(id)
                    .executeAsOne()
                )
    }

    override fun saveIcon(iconDto: IconDto): Long {
        var id: Long = 0
        database.transaction {
            database.iconQueries.insert(iconDto.iconCategoryId, iconDto.raw)
            id = database.generalQueries.lastInsertRowId().executeAsOne()
        }

        return id
    }

    override fun deleteIcon(id: Long) {
        database.transaction {
            database.iconQueries.delete(id)
        }
    }

    override fun updateIcon(iconDto: IconDto) {
        database.transaction {
            database.iconQueries.update(iconDto.iconCategoryId, iconDto.raw, iconDto.id)
        }
    }

    override fun getAllPorts(): List<PortDto> {
        return database
            .portQueries
            .selectAll()
            .executeAsList()
            .map(portSnapshotToPortDtoMapper::map)
    }

    override fun updatePort(port: PortDto): Long {
        var id: Long = 0
        database.transaction {
            database.portQueries.delete(port.id)
            val canRead = if (port.canRead) { 1L } else { 0L }
            val canWrite = if (port.canWrite) { 1L } else { 0L }
            database.portQueries.insert(port.id, port.factoryId, port.adapterId, port.valueType, canRead, canWrite)
            id = database.generalQueries.lastInsertRowId().executeAsOne()
        }

        return id
    }

    override fun deletePort(id: String) {
        database.transaction {
            database.portQueries.delete(id)
        }
    }

    override fun deletePortSnapshot(id: String) {
        database.transaction {
            database.portQueries.delete(id)
        }
    }

    override fun updateSettings(settingsDtos: List<SettingsDto>) {
        database.transaction {
            settingsDtos.forEach { settingDto ->
                settingDto.fields.forEach { field ->
                    database
                        .settingsFieldInstanceQueries
                        .insertOrReplace(settingDto.pluginId, settingDto.clazz, field.key, field.value)
                }
            }
        }
    }

    override fun getSettingsByPluginId(pluginId: String): List<SettingsDto> {
        val result = database
            .settingsFieldInstanceQueries
            .selectByPluginId(pluginId)
            .executeAsList()

        return settingsFieldInstanceListToSettingsDtoList.map(result)
    }

    override fun getSettingsByPluginIdAndClazz(pluginId: String, clazz: String): SettingsDto? {
        val result = database
            .settingsFieldInstanceQueries
            .selectByPluginIdAndClazz(pluginId, clazz)
            .executeAsList()

        return settingsFieldInstanceListToSettingsDtoList
            .map(result)
            .firstOrNull()
    }

    override fun getAllInboxItems(): List<InboxItemDto> {
        return database
            .inboxQueries
            .selectAll()
            .executeAsList()
            .map(inboxItemToInboxItemDtoMapper::map)
    }

    override fun saveInboxItem(inboxItemDto: InboxItemDto): Long {
        database.transaction {
            database.inboxQueries.insert(
                inboxItemDto.timestamp,
                inboxItemDto.kind,
                inboxItemDto.message)
        }

        return database.generalQueries.lastInsertRowId().executeAsOne()
    }

    override fun markInboxItemAsRead(id: Long) {
        database.inboxQueries.markRead(id)
    }

    override fun deleteInboxItem(id: Long) {
        database.inboxQueries.delete(id)
    }
}
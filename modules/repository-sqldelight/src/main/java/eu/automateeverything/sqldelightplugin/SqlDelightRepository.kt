/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.sqldelightplugin

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.automateeverything.data.InstanceInterceptor
import eu.automateeverything.data.Repository
import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.data.icons.IconCategoryDto
import eu.automateeverything.data.icons.IconDto
import eu.automateeverything.data.inbox.InboxItemDto
import eu.automateeverything.data.instances.InstanceBriefDto
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.data.tags.TagDto
import eu.automateeverything.sqldelightplugin.database.*
import java.util.concurrent.CopyOnWriteArrayList

class SqlDelightRepository : Repository {

    private var database: Database
    private var hasUpdatedInstance = false
    private val instanceInterceptors = CopyOnWriteArrayList<InstanceInterceptor>()

    init {
        Class.forName("org.sqlite.JDBC")
        val driver = JdbcSqliteDriver("jdbc:sqlite:repository.sqlite")
        Database.Schema.create(driver)
        driver.execute(null, "PRAGMA foreign_keys=ON", 0)
        database = Database(
            driver
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

    override fun saveInstance(instanceDto: InstanceDto) : Long {
        var id: Long = 0
        database.transaction {
            database.configurableInstanceQueries.insert(instanceDto.clazz, instanceDto.iconId, instanceDto.automation)
            id = database.generalQueries.lastInsertRowId().executeAsOne()

            val insertedDtoId = database.generalQueries.lastInsertRowId().executeAsOne()
            instanceDto.fields.forEach {
                database.configurableFieldInstanceQueries.insert(insertedDtoId, it.key, it.value)
            }

            instanceDto.tagIds.forEach {
                database.instanceTaggingQueries.insert(it, insertedDtoId)
            }
        }

        instanceInterceptors.forEach { it.changed(InstanceInterceptor.Action.Saved, instanceDto.clazz) }

        return id
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

        instanceInterceptors.forEach { it.changed(InstanceInterceptor.Action.Updated, instanceDto.clazz) }
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

        instanceInterceptors.forEach { it.changed(InstanceInterceptor.Action.Deleted, null) }
    }

    override fun deleteInstances(ids: List<Long>) {
        database.transaction {
            ids.forEach {
                database.configurableInstanceQueries.delete(it)
            }
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

    override fun addInstanceInterceptor(interceptor: InstanceInterceptor) {
        instanceInterceptors.add(interceptor)
    }

    override fun removeInstanceInterceptor(interceptor: InstanceInterceptor) {
        instanceInterceptors.remove(interceptor)
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
            database.iconCategoryQueries.insert(
                iconCategoryDto.name.serialize(),
                if (iconCategoryDto.readonly) 1 else 0
            )
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
            database.iconCategoryQueries.update(iconCategoryDto.name.serialize(), iconCategoryDto.id)
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
            database.iconQueries.insert(iconDto.iconCategoryId, iconDto.owner, iconDto.raw)
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

    override fun getPortById(id: String): PortDto? {
        val portSnapshot = database
            .portQueries
            .selectById(id)
            .executeAsOneOrNull()

        return if (portSnapshot != null) {
            portSnapshotToPortDtoMapper.map(portSnapshot)
        } else {
            null
        }
    }

    override fun updatePort(port: PortDto): Long {
        var id: Long = 0
        database.transaction {
            database.portQueries.delete(port.id)
            val canRead = if (port.canRead) { 1L } else { 0L }
            val canWrite = if (port.canWrite) { 1L } else { 0L }
            database.portQueries.insert(port.id, port.factoryId, port.adapterId, port.valueClazz, canRead, canWrite)
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

    override fun getInboxItems(limit: Long, offset: Long): List<InboxItemDto> {
        return database
            .inboxQueries
            .selectByPage(limit, offset)
            .executeAsList()
            .map(inboxItemToInboxItemDtoMapper::map)
    }

    override fun getUnreadInboxItems() : List<InboxItemDto> {
        return database
            .inboxQueries
            .selectUnread()
            .executeAsList()
            .map(inboxItemToInboxItemDtoMapper::map)
    }

    override fun saveInboxItem(message: InboxItemDto): Long {
        database.transaction {
            database.inboxQueries.insert(
                message.timestamp,
                message.subject,
                message.body,
                if (message.read) 1 else 0)
        }

        return database.generalQueries.lastInsertRowId().executeAsOne()
    }

    override fun markInboxItemAsRead(id: Long) : InboxItemDto {
        database.inboxQueries.markRead(id)
        val inboxItem = database.inboxQueries.selectById(id).executeAsOne()
        return inboxItemToInboxItemDtoMapper.map(inboxItem)
    }

    override fun deleteInboxItem(id: Long) {
        database.inboxQueries.delete(id)
    }

    override fun countInboxItems(): Long {
        return database.inboxQueries.countItems().executeAsOne()
    }
}
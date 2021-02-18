package eu.geekhome.sqldelightplugin

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.geekhome.services.repository.*
import eu.geekhome.sqldelightplugin.database.*
import org.pf4j.Extension

@Extension
class SqlDelightRepository : Repository {

    private var database: Database

    init {
        Class.forName("org.sqlite.JDBC")
        val driver = JdbcSqliteDriver("jdbc:sqlite:repository.sqlite")
        Database.Schema.create(driver)
        driver.execute(null, "PRAGMA foreign_keys=ON", 0)
        database = Database(driver)
    }

    private fun convertStringOfIdsToList(input: String): List<Long> {
        return if (input.isNotEmpty()) {
            input.split(',').map { it.toLong() }
        } else {
            ArrayList()
        }
    }

    private fun mapSelectAllShortToConfigurableBriefDto(configurableShort: SelectAllShort) : InstanceBriefDto {
        return InstanceBriefDto(configurableShort.id, configurableShort.clazz, configurableShort.value)
    }

    private fun mapConfigurableInstanceToInstanceDto(configurableInstance: ConfigurableInstanceWithTagIds) : InstanceDto {
        val fieldsMap = HashMap<String, String?>()

        database
            .fieldInstanceQueries
            .selectOfConfigurableInstance(configurableInstance.id)
            .executeAsList()
            .forEach { fieldsMap[it.name] = it.value }

        return InstanceDto(
            configurableInstance.id,
            configurableInstance.icon_id,
            convertStringOfIdsToList(configurableInstance.tagIds),
            configurableInstance.clazz,
            fieldsMap,
            configurableInstance.automation
        )
    }

    private fun mapIconToIconDto(icon: Icon): IconDto {
        return IconDto(icon.id, icon.icon_category_id, icon.raw)
    }

    override fun saveInstance(instanceDto: InstanceDto) {
        database.transaction {
            database.configurableInstanceQueries.insert(instanceDto.clazz, instanceDto.iconId, instanceDto.automation)
            val insertedDtoId = database.generalQueries.lastInsertRowId().executeAsOne()
            instanceDto.fields.forEach {
                database.fieldInstanceQueries.insert(insertedDtoId, it.key, it.value)
            }

            instanceDto.tagIds.forEach {
                database.instanceTaggingQueries.insert(it, insertedDtoId)
            }
        }
    }

    override fun updateInstance(instanceDto: InstanceDto) {
        database.transaction {
            database.configurableInstanceQueries.update(instanceDto.clazz, instanceDto.iconId, instanceDto.id)
            instanceDto.fields.forEach {
                database.fieldInstanceQueries.update(it.value, it.key, instanceDto.id)
            }

            database.instanceTaggingQueries.deleteAllOfInstance(instanceDto.id)
            instanceDto.tagIds.forEach {
                database.instanceTaggingQueries.insert(it, instanceDto.id)
            }
        }
    }

    override fun getAllInstances(): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectAll()
                .executeAsList()
                .map { mapConfigurableInstanceToInstanceDto(it) }
    }

    override fun getAllInstanceBriefs(): List<InstanceBriefDto> {
        return database
                .configurableInstanceQueries
                .selectAllShort()
                .executeAsList()
                .map(this::mapSelectAllShortToConfigurableBriefDto)
    }

    override fun getInstancesOfClazz(clazz: String): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectByClazz(clazz)
                .executeAsList()
                .map(this::mapConfigurableInstanceToInstanceDto)
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

        return mapConfigurableInstanceToInstanceDto(instance)
    }

    override fun getAllTags(): List<TagDto> {
        fun mapTagToTagDto(tag: Tag): TagDto {
            return TagDto(tag.id, tag.parent_id, tag.name)
        }

        return database
                .tagQueries
                .selectAll()
                .executeAsList()
                .map { mapTagToTagDto(it) }
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

        fun mapIconCategoryToIconCategoryDto(iconCategory: SelectAllWithIcons): IconCategoryDto {
            val iconIds = convertStringOfIdsToList(iconCategory.iconIds)
            return IconCategoryDto(iconCategory.id, iconCategory.name, iconIds)
        }

        return database
                .iconCategoryQueries
                .selectAllWithIcons()
                .executeAsList()
                .map { mapIconCategoryToIconCategoryDto(it) }
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
                .map { mapIconToIconDto(it) }
    }

    override fun getIcon(id: Long): IconDto {
        return mapIconToIconDto(database
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
}

package eu.geekhome.sqldelightplugin

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.geekhome.services.repository.InstanceDto
import eu.geekhome.services.repository.Repository
import eu.geekhome.services.repository.TagDto
import eu.geekhome.sqldelightplugin.database.ConfigurableInstance
import eu.geekhome.sqldelightplugin.database.Database
import eu.geekhome.sqldelightplugin.database.Tag
import org.pf4j.Extension

@Extension
class SqlDelightRepository : Repository {

    private var database: Database

    init {
        Class.forName("org.sqlite.JDBC")
        val driver = JdbcSqliteDriver("jdbc:sqlite:repository.sqlite")
        Database.Schema.create(driver)
        database = Database(driver)
    }

    override fun saveInstance(instanceDto: InstanceDto) {
        database.transaction {
            database.configurableInstanceQueries.insert(instanceDto.clazz)
            val insertedDtoId = database.generalQueries.lastInsertRowId().executeAsOne()
            instanceDto.fields.forEach {
                database.fieldInstanceQueries.insert(insertedDtoId, it.key, it.value)
            }
        }
    }

    override fun getAllInstances(): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectAll()
                .executeAsList()
                .map(this::mapConfigurableInstanceToInstanceDto)
    }

    override fun getInstancesOfClazz(clazz: String): List<InstanceDto> {
        return database
                .configurableInstanceQueries
                .selectByClazz(clazz)
                .executeAsList()
                .map(this::mapConfigurableInstanceToInstanceDto)
    }

    override fun getInstance(id: Long): InstanceDto {
        val instance = database
                .configurableInstanceQueries
                .selectById(id)
                .executeAsOne();

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

    private fun mapConfigurableInstanceToInstanceDto(configurableInstance: ConfigurableInstance) : InstanceDto {
        val fieldsMap = HashMap<String, String?>()

        database
                .fieldInstanceQueries
                .selectOfConfigurableInstance(configurableInstance.id)
                .executeAsList()
                .forEach { fieldsMap[it.name] = it.value }

        return InstanceDto(configurableInstance.id, configurableInstance.clazz, fieldsMap)
    }
}

package eu.geekhome.sqldelightplugin

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.geekhome.services.repository.InstanceDto
import eu.geekhome.services.repository.Repository
import eu.geekhome.sqldelightplugin.database.ConfigurableInstance
import eu.geekhome.sqldelightplugin.database.Database
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

package eu.geekhome.sqldelightplugin

import eu.geekhome.services.repository.InstanceDto
import eu.geekhome.services.repository.Repository
import org.pf4j.Extension

@Extension
class SqlDelightRepository : Repository {
    override fun saveInstance(instanceDto: InstanceDto) {

    }
}

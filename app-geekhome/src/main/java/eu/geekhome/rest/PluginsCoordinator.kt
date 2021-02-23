package eu.geekhome.rest

import eu.geekhome.services.configurable.Configurable
import eu.geekhome.services.repository.Repository
import org.jvnet.hk2.annotations.Service
import org.pf4j.PluginManager
import org.pf4j.PluginWrapper
import org.pf4j.PluginStateListener
import javax.ws.rs.core.Application
import javax.ws.rs.core.Context

fun PluginManager.getConfigurables(): List<Configurable> {
    return this.getExtensions(Configurable::class.java)
}

fun PluginManager.getRepository(): Repository {
    return this.getExtensions(Repository::class.java)[0]
}

@Service
class PluginsCoordinator(@Context app: Application) : HolderService<PluginManager>(app, PluginManager::class.java) {
    val plugins: List<PluginWrapper>
        get() = instance.plugins

    fun getPlugin(id: String?): PluginWrapper {
        return instance.getPlugin(id)
    }

    fun enablePlugin(id: String?): PluginWrapper {
        instance.enablePlugin(id)
        instance.startPlugin(id)
        return getPlugin(id)
    }

    fun disablePlugin(id: String?): PluginWrapper {
        instance.stopPlugin(id)
        instance.disablePlugin(id)
        return getPlugin(id)
    }

    fun registerStateListener(listener: PluginStateListener?) {
        instance.addPluginStateListener(listener)
    }

    val configurables: List<Configurable>
        get() = instance.getConfigurables()

    val repository: Repository
        get() = instance.getRepository()
}
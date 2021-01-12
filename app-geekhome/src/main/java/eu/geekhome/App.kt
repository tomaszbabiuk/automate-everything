package eu.geekhome

import org.glassfish.jersey.server.ResourceConfig
import org.pf4j.JarPluginManager
import org.pf4j.ExtensionFactory
import org.pf4j.SingletonExtensionFactory
import eu.geekhome.rest.DependencyInjectionBinder
import eu.geekhome.rest.GsonMessageBodyHandler
import eu.geekhome.rest.CORSFilter
import org.pf4j.PluginManager

class App : ResourceConfig() {
    private fun startPlugins(pluginManager: PluginManager) {
        pluginManager.loadPlugins()
        pluginManager.startPlugins()
    }

    private fun buildPluginManager(): PluginManager {
        return object : JarPluginManager() {
            override fun createExtensionFactory(): ExtensionFactory {
                return SingletonExtensionFactory()
            }
        }
    }

    init {
        val pluginManager = buildPluginManager()
        startPlugins(pluginManager)
        val hardwareManager = HardwareManager(pluginManager)
        packages("eu.geekhome.rest")
        register(DependencyInjectionBinder())
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(pluginManager)
        register(hardwareManager)

        hardwareManager.discover()
    }
}
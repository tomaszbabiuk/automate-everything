package eu.geekhome

import org.glassfish.jersey.server.ResourceConfig
import org.pf4j.JarPluginManager
import org.pf4j.ExtensionFactory
import org.pf4j.SingletonExtensionFactory
import eu.geekhome.rest.DependencyInjectionBinder
import eu.geekhome.rest.GsonMessageBodyHandler
import eu.geekhome.rest.CORSFilter
import eu.geekhome.rest.ResourceNotFoundExceptionMapper
import org.pf4j.PluginManager

class App : ResourceConfig() {
    private fun buildPluginManager(): PluginManager {
        return object : JarPluginManager() {
            override fun createExtensionFactory(): ExtensionFactory {
                return SingletonExtensionFactory()
            }
        }
    }

    init {
        val pluginManager = buildPluginManager()
        pluginManager.loadPlugins()

        val hardwareManager = HardwareManager(pluginManager)
        packages("eu.geekhome.rest")
        register(DependencyInjectionBinder())
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())
        register(pluginManager)
        register(hardwareManager)

        pluginManager.startPlugins()
    }
}
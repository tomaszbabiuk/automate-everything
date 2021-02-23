package eu.geekhome

import eu.geekhome.rest.*
import org.glassfish.jersey.server.ResourceConfig
import org.pf4j.JarPluginManager
import org.pf4j.ExtensionFactory
import org.pf4j.SingletonExtensionFactory
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
        val automationConductor = AutomationConductor(hardwareManager, pluginManager)

        packages("eu.geekhome.rest")
        register(DependencyInjectionBinder())
        register(GsonMessageBodyHandler())
        register(CORSFilter())
        register(ResourceNotFoundExceptionMapper())
        register(pluginManager)
        register(hardwareManager)
        register(automationConductor)

        pluginManager.startPlugins()
    }
}
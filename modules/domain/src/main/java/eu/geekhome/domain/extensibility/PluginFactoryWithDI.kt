package eu.geekhome.domain.extensibility

import org.pf4j.Plugin
import org.pf4j.PluginFactory
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory
import java.lang.reflect.Modifier

class PluginFactoryWithDI(private val injectionRegistry: InjectionRegistry) : PluginFactory {

    private val log = LoggerFactory.getLogger(PluginFactoryWithDI::class.java)

    override fun create(pluginWrapper: PluginWrapper): Plugin? {
        val pluginClassName = pluginWrapper.descriptor.pluginClass

        val pluginClass: Class<*> = try {
            pluginWrapper.pluginClassLoader.loadClass(pluginClassName)
        } catch (e: ClassNotFoundException) {
            log.error(e.message, e)
            return null
        }

        val modifiers = pluginClass.modifiers
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)
            || !Plugin::class.java.isAssignableFrom(pluginClass)
        ) {
            log.error("The plugin class '{}' is not valid", pluginClassName)
            return null
        }

        return try {
            createWithDI(pluginClass, pluginWrapper)
        } catch (ex: CreationException) {
            log.error("There's been a problem creating plugin class '{}' is not valid", pluginClassName, ex)
            null
        }
    }

    private fun createWithDI(pluginClass: Class<*>, pluginWrapper: PluginWrapper): Plugin {
        val constructors = pluginClass.constructors
        if (constructors.size == 1) {
            val primaryConstructor = constructors[0]
            val injectionParameters = primaryConstructor
                .parameters
                .map {
                    val constructedType = it.type

                    if (it.type == PluginWrapper::class.java) {
                        pluginWrapper
                    } else {
                        val obj = injectionRegistry.resolve(constructedType)
                            ?: throw CreationException("Cannot inject ${pluginClass.name}, the constructor contains parameter of unknown type $constructedType")
                        obj
                    }
                }.toTypedArray()

            val created = primaryConstructor.newInstance(*injectionParameters)
            return created as Plugin
        }

        throw CreationException("Plugin should have only one constructor!")
    }
}
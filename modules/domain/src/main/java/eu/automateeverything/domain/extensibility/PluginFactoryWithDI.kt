/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
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

package eu.automateeverything.domain.extensibility

import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.settings.SettingsResolver
import org.pf4j.Plugin
import org.pf4j.PluginFactory
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory
import java.lang.reflect.Modifier

class PluginFactoryWithDI(
    private val injectionRegistry: InjectionRegistry,
    private val settingsExtractor: SettingsExtractor) : PluginFactory {

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
            val pluginSpecificSettingsResolver = object : SettingsResolver {
                override fun resolve(): List<SettingsDto> {
                    return settingsExtractor.extractSettings(pluginWrapper.pluginId)
                }
            }
            val primaryConstructor = constructors[0]
            val injectionParameters = primaryConstructor
                .parameters
                .map {
                    val constructedType = it.type

                    if (it.type == PluginWrapper::class.java) {
                        pluginWrapper
                    } else if (constructedType == SettingsResolver::class.java) {
                        pluginSpecificSettingsResolver
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
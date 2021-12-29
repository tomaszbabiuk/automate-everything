/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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
import org.pf4j.DefaultExtensionFactory
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingletonExtensionFactoryWithDI(
    private val injectionRegistry: InjectionRegistry,
    private val settingsExtractor: SettingsExtractor) : DefaultExtensionFactory() {

    private val cache: MutableMap<ClassLoader, MutableMap<String, Any>> = WeakHashMap()

    override fun <T : Any> create(extensionClass: Class<T>): T {

        val extensionClassName = extensionClass.name
        val extensionClassLoader = extensionClass.classLoader

        if (!cache.containsKey(extensionClassLoader)) {
            cache[extensionClassLoader] = HashMap<String, Any>()
        }

        val classLoaderBucket = cache[extensionClassLoader]!!

        if (classLoaderBucket.containsKey(extensionClassName)) {
            return (classLoaderBucket[extensionClassName] as T)
        }

        val extension = createWithDI(extensionClass)
        classLoaderBucket[extensionClassName] = extension

        return extension
    }

    private fun <T> createWithDI(extensionClass: Class<T>): T {
        val constructors = extensionClass.constructors
        if (constructors.size == 1) {
            val pluginSpecificSettingsResolver = object : SettingsResolver {
                override fun resolve(): List<SettingsDto> {
                    return settingsExtractor.extractSettings(extensionClass)
                }
            }
            val primaryConstructor = constructors[0]
            val injectionParameters = primaryConstructor
                .parameters
                .map {
                    val constructedType = it.type

                    if (constructedType == SettingsResolver::class.java) {
                        pluginSpecificSettingsResolver
                    } else {
                        val obj = injectionRegistry.resolve(constructedType)
                            ?: throw CreationException("Cannot inject ${extensionClass.name}, the constructor contains parameter of unknown type $constructedType")
                        obj
                    }
                }.toTypedArray()

            val created = primaryConstructor.newInstance(*injectionParameters)
            return created as T
        }

        //no DI
        return super.create(extensionClass)
    }
}
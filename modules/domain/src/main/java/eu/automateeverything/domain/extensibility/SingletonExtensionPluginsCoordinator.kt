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

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.HardwareAdapter
import org.pf4j.*

class SingletonExtensionPluginsCoordinator(
    private val liveEvents: EventBus,
    private val injectionRegistry: InjectionRegistry,
    repository: Repository
) : PluginsCoordinator {

    val extractor = SettingsExtractor(this, repository)

    private val wrapped: JarPluginManager =
        object : JarPluginManager(), PluginStateListener {
            override fun createExtensionFactory(): ExtensionFactory {
                return SingletonExtensionFactoryWithDI(injectionRegistry, extractor)
            }

            override fun createPluginFactory(): PluginFactory {
                return PluginFactoryWithDI(injectionRegistry, extractor)
            }

            init {
                addPluginStateListener(this)
            }

            override fun pluginStateChanged(event: PluginStateEvent) {
                val plugin = event.plugin
                liveEvents.broadcastPluginEvent(plugin)
            }
        }

    override fun getPluginWrapper(pluginId: String): PluginWrapper? {
        return wrapped.getPlugin(pluginId)
    }

    override fun getPluginSettingGroups(pluginId: String): List<SettingGroup> {
        val pluginWrapper = getPluginWrapper(pluginId) ?: return listOf()
        val metadata = pluginWrapper.plugin as PluginMetadata
        return metadata.settingGroups
    }

    override fun getPluginSettingGroups(pluginWrapper: PluginWrapper): List<SettingGroup> {
        val metadata = pluginWrapper.plugin as PluginMetadata
        return metadata.settingGroups
    }

    override fun findExtensionOwner(extensionClazz: Class<*>): PluginWrapper? {
        return wrapped.plugins.firstOrNull {
            wrapped.getExtensionClasses(it.pluginId).contains(extensionClazz)
        }
    }

    override fun enablePlugin(pluginId: String): PluginWrapper? {
        if (wrapped.enablePlugin(pluginId)) {
            wrapped.startPlugin(pluginId)
            return getPluginWrapper(pluginId)
        }

        return null
    }

    override fun disablePlugin(pluginId: String): PluginWrapper? {
        if (wrapped.disablePlugin(pluginId)) {
            return getPluginWrapper(pluginId)
        }

        return null
    }

    override fun loadPlugins() {
        wrapped.loadPlugins()
    }

    override fun startPlugins() {
        wrapped.startPlugins()
    }

    override fun addPluginStateListener(listener: PluginStateListener) {
        wrapped.addPluginStateListener(listener)
    }

    override val configurables: List<Configurable>
        get() = wrapped.getExtensions(Configurable::class.java)

    override val blockFactories: List<BlockFactory<*, *, *>>
        get() = wrapped.getExtensions(BlockFactory::class.java) as List<BlockFactory<*, *, *>>

    override val blockFactoriesCollectors: List<BlockFactoriesCollector>
        get() = wrapped.getExtensions(BlockFactoriesCollector::class.java)

    override val hardwareAdapters: List<HardwareAdapter<*>>
        get() = wrapped.getExtensions(HardwareAdapter::class.java)

    override val plugins: List<PluginWrapper>
        get() = wrapped.plugins
}

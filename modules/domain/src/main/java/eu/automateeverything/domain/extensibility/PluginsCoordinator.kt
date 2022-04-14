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

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.BlockFactoriesCollector
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.hardware.HardwareAdapter
import org.pf4j.PluginStateListener
import org.pf4j.PluginWrapper

interface PluginsCoordinator {
    fun enablePlugin(pluginId: String): PluginWrapper?
    fun disablePlugin(pluginId: String): PluginWrapper?
    fun loadPlugins()
    fun startPlugins()
    fun addPluginStateListener(listener: PluginStateListener)
    fun getPluginWrapper(pluginId: String): PluginWrapper?
    fun getPluginSettingGroups(pluginId: String) : List<SettingGroup>
    fun getPluginSettingGroups(pluginWrapper: PluginWrapper) : List<SettingGroup>
    fun findExtensionOwner(extensionClazz: Class<*>): PluginWrapper?
    val plugins: List<PluginWrapper>
    val configurables: List<Configurable>
    val blockFactories: List<BlockFactory<*>>
    val blockFactoriesCollectors: List<BlockFactoriesCollector>
    val hardwareAdapters: List<HardwareAdapter<*>>
}
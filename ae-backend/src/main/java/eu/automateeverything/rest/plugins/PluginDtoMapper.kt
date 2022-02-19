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

package eu.automateeverything.rest.plugins

import eu.automateeverything.R
import eu.automateeverything.data.plugins.PluginDto
import eu.automateeverything.rest.settinggroup.SettingGroupDtoMapper
import org.pf4j.PluginWrapper
import org.pf4j.PluginState
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.extensibility.PluginMetadata
import jakarta.inject.Inject

class PluginDtoMapper @Inject constructor(
    private val settingsGroupDtoMapper: SettingGroupDtoMapper,
) {
    fun map(pluginWrapper: PluginWrapper): PluginDto {
        val plugin = pluginWrapper.plugin
        val id = pluginWrapper.pluginId
        val version = pluginWrapper.descriptor.version
        val provider = pluginWrapper.descriptor.provider
        val enabled = pluginWrapper.pluginState == PluginState.STARTED
        val isHardwareFactory = pluginWrapper.plugin is HardwarePlugin
        return if (plugin is PluginMetadata) {
            val metadata = plugin as PluginMetadata
            val settingGroups = metadata.settingGroups.map { settingsGroupDtoMapper.map(it) }
            PluginDto(id, metadata.name, metadata.description, metadata.copyright, provider, version, isHardwareFactory, enabled, settingGroups)
        } else {
            PluginDto(id, R.plugin_no_name, R.plugin_no_description, null, provider, version, isHardwareFactory, enabled, listOf())
        }
    }
}
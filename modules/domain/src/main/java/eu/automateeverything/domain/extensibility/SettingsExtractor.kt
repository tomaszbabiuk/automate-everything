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
import eu.automateeverything.data.settings.SettingsDto

class SettingsExtractor(
    private val pluginsCoordinator: PluginsCoordinator,
    private val repository: Repository) {

    fun extractSettings(extensionClazz: Class<*>): List<SettingsDto> {
        val pluginContainingThisExtension = pluginsCoordinator.findExtensionOwner(extensionClazz)

        return extractSettings(pluginContainingThisExtension!!.pluginId)
    }

    fun extractSettings(pluginId: String): List<SettingsDto> {
        return pluginsCoordinator
            .getPluginSettingGroups(pluginId)
            .mapNotNull { repository.getSettingsByPluginIdAndClazz(pluginId, it.javaClass.name) }
    }
}
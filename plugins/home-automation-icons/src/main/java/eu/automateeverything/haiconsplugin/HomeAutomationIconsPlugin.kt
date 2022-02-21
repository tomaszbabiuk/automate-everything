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

package eu.automateeverything.haiconsplugin

import eu.automateeverything.data.Repository
import eu.automateeverything.data.icons.IconCategoryDto
import eu.automateeverything.data.icons.IconDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.plugins.PluginCategory
import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import java.util.*


class HomeAutomationIconsPlugin(
    wrapper: PluginWrapper,
    private val repository: Repository
) : Plugin(wrapper), PluginMetadata {

    val icons = listOf(
        "attic",
        "averagingthermometer",
        "bell",
        "blinds",
        "boiler",
        "brightness",
        "bullhorn",
        "button",
        "clock",
        "comfortmeter",
        "coordination",
        "curtain",
        "desklamp",
        "door",
        "dust",
        "equalizer",
        "fan",
        "faucet",
        "garage",
        "garagedoor",
        "gate",
        "gauge",
        "hanginglamp",
        "heat",
        "house",
        "hygrometer",
        "impeller",
        "key",
        "keyboard",
        "leaf",
        "led",
        "lightbulb",
        "lightswitch",
        "lock",
        "luminosity",
        "magneticdetector",
        "meter",
        "microchip",
        "monitoring",
        "moon",
        "plant",
        "plugin",
        "power",
        "radiator",
        "remotecontroller",
        "rgblamp",
        "rtl",
        "running",
        "shield",
        "smartphone",
        "smokedetector",
        "snowflake",
        "speaker",
        "standinglamp",
        "sun",
        "switch",
        "thermometer",
        "thermostat",
        "tv",
        "valve",
        "washingmachine",
        "watertreatmentplant",
        "weatherizedhome",
    )

    override fun start() {
        val allIcons = repository.getAllIcons()

        val isInstalled = allIcons.filter { it.owner == ICON_OWNER }

        if (isInstalled.isEmpty()) {
            val categoryId = repository.saveIconCategory(
                IconCategoryDto(0, R.plugin_name, true, listOf())
            )

            icons.forEach {
                val svg = loadTextFromResources("$it.svg")
                val iconDto = IconDto(0, categoryId, ICON_OWNER, svg!!)
                repository.saveIcon(iconDto)
            }
        }
    }

    override fun stop() {
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val category: PluginCategory = PluginCategory.Icons
    override val copyright: Resource = R.plugin_copyright

    private fun loadTextFromResources(path: String): String? {
        val stream = javaClass.classLoader.getResourceAsStream(path)
        val s = Scanner(stream!!).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }

    companion object {
        const val ICON_OWNER = "home-automation-plugin"
    }
}
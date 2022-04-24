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

package eu.automateeverything.shellyplugin

import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwarePlugin
import eu.automateeverything.domain.langateway.LanGatewayResolver
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.plugins.PluginCategory
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.mqtt.MqttBrokerService
import org.pf4j.PluginWrapper
import java.util.ArrayList

class ShellyPlugin(
    wrapper: PluginWrapper,
    private val lanGatewayResolver: LanGatewayResolver,
    private val mqttBroker: MqttBrokerService,
    private val eventBus: EventBus)
    : HardwarePlugin(wrapper), PluginMetadata{

    override fun start() {
    }

    override fun stop() {
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val result = ArrayList<HardwareAdapter<*>>()
        val adapter = ShellyAdapter(pluginId, mqttBroker, lanGatewayResolver, eventBus)
        result.add(adapter)
        return result
    }

    override val name: Resource =  R.plugin_name
    override val description: Resource = R.plugin_description
    override val category: PluginCategory = PluginCategory.Hardware
}
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

package eu.automateeverything.mobileaccessplugin

import eu.automateeverything.data.InstanceInterceptor
import eu.automateeverything.data.Repository
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.domain.settings.SettingsResolver
import eu.automateeverything.interop.ByteArraySessionHandler
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class MobileAccessPlugin(wrapper: PluginWrapper,
                         settingsResolver: SettingsResolver,
                         private val repository: Repository,
                         private val sessionHandler: ByteArraySessionHandler,
                         private val inbox: Inbox
) : Plugin(wrapper), PluginMetadata, InstanceInterceptor {
    val server: MqttSaltServer by lazy {
        createServer(settingsResolver)
    }

    private fun createServer(settingsResolver: SettingsResolver): MqttSaltServer {
        val settings = settingsResolver.resolve()
        val brokerAddress = extractBrokerAddress(settings)
        val secretsPassword = extractSecretsPassword(settings)
        return MqttSaltServer(brokerAddress, secretsPassword, inbox, sessionHandler, repository)
    }

    override fun start() {
        repository.addInstanceInterceptor(this)
        server.start(loadPublicKeysFromRepository())
    }

    override fun stop() {
        server.stop()
        repository.removeInstanceInterceptor(this)
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description

    override val settingGroups = listOf(SecretsProtectionSettingGroup())

    private fun extractBrokerAddress(pluginSettings: List<SettingsDto>): String {
        return if (pluginSettings.size == 1) {
            pluginSettings[0].fields[SecretsProtectionSettingGroup.FIELD_MQTT_BROKER_ADDRESS]!!
        } else {
            SecretsProtectionSettingGroup.DEFAULT_MQTT_BROKER_ADDRESS
        }
    }

    private fun extractSecretsPassword(pluginSettings: List<SettingsDto>): String {
        return if (pluginSettings.size == 1) {
            pluginSettings[0].fields[SecretsProtectionSettingGroup.FIELD_PASSWORD]!!
        } else {
            SecretsProtectionSettingGroup.DEFAULT_PASSWORD
        }
    }

    private fun loadPublicKeysFromRepository(): List<String> {
        return repository
            .getInstancesOfClazz(MobileCredentialsConfigurable::class.java.name)
            .map { it.fields[MobileCredentialsConfigurable.FIELD_SERVER_PUB]!! }
    }

    override fun changed(action: InstanceInterceptor.Action) {
        println("The number of instances has changed... stopping server")
        server.stop()

        println("The number of instances has changed... starting server")
        server.start(loadPublicKeysFromRepository())
    }
}
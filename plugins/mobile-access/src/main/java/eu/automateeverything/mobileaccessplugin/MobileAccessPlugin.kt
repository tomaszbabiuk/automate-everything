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
import eu.automateeverything.data.plugins.PluginCategory
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.events.EventBus
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.inbox.Inbox
import eu.automateeverything.domain.settings.SettingsResolver
import eu.automateeverything.interop.ByteArraySessionHandler
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

class MobileAccessPlugin(wrapper: PluginWrapper,
                         private val settingsResolver: SettingsResolver,
                         private val repository: Repository,
                         private val sessionHandler: ByteArraySessionHandler,
                         private val inbox: Inbox,
                         private val eventBus: EventBus
) : Plugin(wrapper), PluginMetadata, InstanceInterceptor {

    private val logger = LoggerFactory.getLogger(MobileAccessPlugin::class.java)
    var server: MqttSaltServer? = null

    private fun createServer(settingsResolver: SettingsResolver): MqttSaltServer {
        val settings = settingsResolver.resolve()
        val brokerAddress = BrokerAddress(settings)
        val secretsPassword = extractSecretsPassword(settings)
        val channelActivator = ChannelActivator(repository, eventBus)
        return MqttSaltServer(brokerAddress, secretsPassword, inbox, sessionHandler, channelActivator)
    }

    override fun start() {
        repository.addInstanceInterceptor(this)
        server = createServer(settingsResolver)
        server?.start(loadPublicKeysFromRepository())
    }

    override fun stop() {
        server?.stop()
        repository.removeInstanceInterceptor(this)
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val category: PluginCategory = PluginCategory.Access

    override val settingGroups = listOf(
        SecretsProtectionSettingGroup(),
        MqttBrokerSettingGroup()
    )

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

    override fun changed(action: InstanceInterceptor.Action, clazz: String?) {
        if (action != InstanceInterceptor.Action.Updated && clazz == MobileCredentialsConfigurable::class.java.name) {
            logger.debug("The number of mobile credentials has changed... restarting server")
            server?.stop()

            server?.start(loadPublicKeysFromRepository())
        }
    }
}
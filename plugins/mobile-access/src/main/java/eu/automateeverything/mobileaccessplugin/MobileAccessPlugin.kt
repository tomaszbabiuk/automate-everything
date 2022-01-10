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

import eu.automateeverything.domain.extensibility.PluginMetadata
import org.pf4j.PluginWrapper
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.data.settings.SettingsDto
import eu.automateeverything.domain.settings.SettingsResolver
import kotlinx.coroutines.*
import org.pf4j.Plugin
import saltchannel.util.Hex
import saltchannel.util.Rand
import saltchannel.v2.SaltServerSession
import java.security.SecureRandom

class MobileAccessPlugin(wrapper: PluginWrapper,
                         private val settingsResolver: SettingsResolver
) : Plugin(wrapper), PluginMetadata {
    var startStopScope = createNewScope()

//    override fun start() {
//
//
//        println(brokerAddress)
//
//        val storage = SecretStorage()
//        val secrets = storage.loadAllSecrets(secretsPassword)
//
//

//        val keypair = KeyPair.fromHex("xx", "xx")
//        val saltServerSession = SaltServerSession(keypair, object: ByteChannel {
//            override fun read(): ByteArray {
//                TODO("Not yet implemented")
//            }
//
//            override fun write(vararg messages: ByteArray) {
//                TODO("Not yet implemented")
//            }
//
//            override fun write(isLast: Boolean, vararg messages: ByteArray?) {
//                TODO("Not yet implemented")
//            }
//        })
//
//    }

    override fun start() {
        startStopScope = createNewScope()

        startStopScope.launch {
            delay(10000)
            val pluginSettings = settingsResolver.resolve()
            val brokerAddress = extractBrokerAddress(pluginSettings)
            val secretsPassword = extractSecretsPassword(pluginSettings)
            val storage = SecretStorage()
            val sessions = storage
                .loadAllSecrets(secretsPassword)
                .map {
                    val topic = String(Hex.toHexCharArray(it.pub(), 0, it.pub().size))
                    val byteChannel = MqttByteChannel(brokerAddress, topic, topic)
                    byteChannel.establishConnection()
                    SaltServerSession(it, byteChannel)
                }

            val random = Rand { b -> SecureRandom.getInstanceStrong().nextBytes(b) }
            sessions.forEach {
                it.setEncKeyPair(random)
                it.handshake()
            }
        }
    }

    override fun stop() {
        startStopScope.cancel("Stopping ${this.javaClass.name}")
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description

    override val settingGroups = listOf(SecretsProtectionSettingGroup())

    private fun createNewScope() : CoroutineScope {
        return CoroutineScope(Dispatchers.IO)
    }

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
}
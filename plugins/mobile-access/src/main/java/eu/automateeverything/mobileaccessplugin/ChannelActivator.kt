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

import eu.automateeverything.data.Repository
import eu.automateeverything.domain.configurable.BooleanFieldBuilder
import saltchannel.util.Hex

class ChannelActivator(private val repository: Repository) {
    fun activateChannel(serverSignPub: ByteArray, clientSigPub: ByteArray) {
        val allMobileCredentials = repository.getInstancesOfClazz(MobileCredentialsConfigurable::class.java.name)
        val serverPubKeyHex = String(Hex.toHexCharArray(serverSignPub, 0, serverSignPub.size))
        val clientPubKeyHex = String(Hex.toHexCharArray(clientSigPub, 0, clientSigPub.size))
        allMobileCredentials
            .filter { it.fields[MobileCredentialsConfigurable.FIELD_SERVER_PUB] == serverPubKeyHex }
            .map {
                val newFields = it.fields.toMutableMap()
                newFields[MobileCredentialsConfigurable.FIELD_ACTIVATED] = BooleanFieldBuilder.TRUE
                newFields[MobileCredentialsConfigurable.FIELD_CLIENT_PUB] = clientPubKeyHex
                newFields[MobileCredentialsConfigurable.FIELD_QR_CODE] = ""
                val instanceToUpdate = it.copy(it.id, it.iconId, it.tagIds, it.clazz, newFields, it.automation)
                repository.updateInstance(instanceToUpdate)
            }
    }
}
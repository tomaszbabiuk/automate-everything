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

package eu.automateeverything.interop

import eu.automateeverything.data.Repository
import eu.automateeverything.data.instances.InstanceDto
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class AESessionHandler(private val repository: Repository) : AccessSessionHandler {

    @OptIn(ExperimentalSerializationApi::class)
    override fun handleIncomingPacket(bytes: ByteArray) : ByteArray {
        val request = Cbor.decodeFromByteArray<JsonRpc2Request>(bytes)
        if (request.id == null) {
            //TODO: handle notification
        } else {
            if (request.method == InstanceDto::class.java.simpleName) {
                val instances = repository.getAllInstances()
                val response = JsonRpc2Response(request.id, instances, null)
                return Cbor.encodeToByteArray(response)
            }
        }

        throw Error("Illegal state")
    }
}
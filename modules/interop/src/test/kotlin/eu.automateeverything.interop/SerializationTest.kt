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

import eu.automateeverything.data.Repository
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.interop.AESessionHandler
import eu.automateeverything.interop.JsonRpc2Response
import eu.automateeverything.interop.createRequestFromType
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.cbor.Cbor.Default.decodeFromByteArray
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test

internal class SerializationTest {

    private val instanceDto1 = InstanceDto(0, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val instanceDto2 = InstanceDto(1, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val mockedInstances = listOf(instanceDto1, instanceDto2)

    private val repositoryMock = mock<Repository> {
        on { getAllInstances() } doReturn mockedInstances
    }

    val tested = AESessionHandler(repository = repositoryMock)

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testSerialization() {
        val instancesRequest = createRequestFromType(InstanceDto::class.java)
        val request1Cbor = Cbor.encodeToByteArray(instancesRequest)
        val responseSerialized = tested.handleIncomingPacket(request1Cbor)
        val responseDeserialized = Cbor.decodeFromByteArray<JsonRpc2Response<List<InstanceDto>>>(responseSerialized)
        println(responseDeserialized)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testSerialization2() {
//        val repositoryTypes = SerializersModule {
////            contextual(ListSerializer(InstanceDto.serializer()))
////            contextual(Any::class) { args -> ListSerializer(args[0]) }
//        }
//        val format = Json { serializersModule = repositoryTypes }
//        val instance1 = InstanceDto(1, 1, listOf(), "clazz1", mapOf(), "automation1")
//        val instance2 = InstanceDto(2, 2, listOf(), "clazz1", mapOf(), "automation2")
//        val response = JsonRpc2Response("ajdi", listOf(instance1, instance2), null)
//        val request1Json = format.encodeToString(response)
//        println(request1Json)
    }
}
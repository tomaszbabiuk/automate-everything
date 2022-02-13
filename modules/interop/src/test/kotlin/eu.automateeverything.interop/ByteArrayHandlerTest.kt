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
import eu.automateeverything.interop.ByteArraySessionHandler
import eu.automateeverything.interop.JsonRpc2Response
import eu.automateeverything.interop.JsonRpc2SessionHandler
import eu.automateeverything.interop.createRequestFromType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
internal class ByteArrayHandlerTest {

    private val instanceDto1 = InstanceDto(0, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val instanceDto2 = InstanceDto(1, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val mockedInstances = listOf(instanceDto1, instanceDto2)

    private val repositoryMock = mock<Repository> {
        on { getAllInstances() } doReturn mockedInstances
    }

    private val processorPart = JsonRpc2SessionHandler(repository = repositoryMock, Cbor)
    private val targetPart = ByteArraySessionHandler(processorPart, Cbor)

    @Serializable
    data class UnknownDto(
        val someEntity: String
    )

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun testExistingMethod() {
        val instancesRequest = createRequestFromType(InstanceDto::class.java, "one")
        val instancesRequest2 = createRequestFromType(InstanceDto::class.java, "two")
        val requests = listOf(instancesRequest, instancesRequest2)
        val requestsSerialized = Cbor.encodeToByteArray(requests)

        val responsesSerialized = targetPart.handleRequest(requestsSerialized)
        val responses = Cbor.decodeFromByteArray<List<JsonRpc2Response>>(responsesSerialized)
        val responseOneSerialised = responses.first { it.id == "one"}
        val responseOne = Cbor.decodeFromByteArray<List<InstanceDto>>(responseOneSerialised.result!!)

        assertEquals(2, responseOne.size)
    }
}
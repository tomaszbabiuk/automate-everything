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
import eu.automateeverything.interop.createRequestFromType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class SerializationTest {

    private val instanceDto1 = InstanceDto(0, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val instanceDto2 = InstanceDto(1, null, listOf(), "ConfigurableClazz", mapOf(), null)
    private val mockedInstances = listOf(instanceDto1, instanceDto2)

    private val repositoryMock = mock<Repository> {
        on { getAllInstances() } doReturn mockedInstances
    }

    private val target = AESessionHandler(repository = repositoryMock, Cbor)

    @Serializable
    data class UnknownDto(
        val someEntity: String
    )

    @Test
    fun testExistingMethod() {
        val instancesRequest = createRequestFromType(InstanceDto::class.java)
        val response = target.handleRequest(instancesRequest)
        println(response)

        val decoded = Cbor.decodeFromByteArray<List<InstanceDto>>(response.result!!)
        assertNotNull(response.result)
        assertNull(response.error)
        assertEquals(2, decoded.size)
    }

    @Test
    fun testFailedMethod() {
        val instancesRequest = createRequestFromType(UnknownDto::class.java)
        val response = target.handleRequest(instancesRequest)
        println(response)

        assertNull(response.result)
        assertNotNull(response.error)
        assertEquals(404, response.error!!.code)
    }
}
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

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.*

@Serializable
data class JsonRpc2Request(
    val jsonRpc: String = "2.0",
    val method: String,
    val id: String? = null,
    val params: List<JsonRpc2Param>? = null
)

@Serializable
enum class JsonRpc2ParamType {
    Int, String
}

@Serializable
data class JsonRpc2Param(
    val name: String,
    val value: String,
    val type: JsonRpc2ParamType
)

@Serializable
data class JsonRpc2Error(
    val code: Long,
    val message: String
)

@Serializable(with = BoxSerializer::class)
data class Box<T>(val contents: List<T>)

class BoxSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<Box<T>> {
    override val descriptor: SerialDescriptor = ListSerializer(dataSerializer).descriptor
    override fun serialize(encoder: Encoder, value: Box<T>) = ListSerializer(dataSerializer).serialize(encoder, value.contents)
    override fun deserialize(decoder: Decoder) = Box(ListSerializer(dataSerializer).deserialize(decoder))
}

@Serializable
data class JsonRpc2Response<T>(
    val id: String,
    @Serializable(with = BoxSerializer::class)
    val result: Box<T>,
    val error: JsonRpc2Error?
)

fun <T> createRequestFromType(clazz: Class<T>): JsonRpc2Request {
    val simpleName = clazz.simpleName
    return JsonRpc2Request(method = simpleName, id = simpleName)
}
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

import kotlinx.serialization.Serializable

@Serializable
data class JsonRpc2Request(
    val jsonRpc: String = "2.0",
    val method: String,
    val id: String? = null,
    val params: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JsonRpc2Request

        if (jsonRpc != other.jsonRpc) return false
        if (method != other.method) return false
        if (id != other.id) return false
        if (params != null) {
            if (other.params == null) return false
            if (!params.contentEquals(other.params)) return false
        } else if (other.params != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = jsonRpc.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (params?.contentHashCode() ?: 0)
        return result
    }
}

@Serializable
data class JsonRpc2Error(
    val code: Long,
    val message: String
)

@Serializable
data class JsonRpc2Response(
    val id: String,
    val result: ByteArray? = null,
    val error: JsonRpc2Error? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JsonRpc2Response

        if (id != other.id) return false
        if (result != null) {
            if (other.result == null) return false
            if (!result.contentEquals(other.result)) return false
        } else if (other.result != null) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result1 = id.hashCode()
        result1 = 31 * result1 + (result?.contentHashCode() ?: 0)
        result1 = 31 * result1 + (error?.hashCode() ?: 0)
        return result1
    }
}

fun <T> createRequestFromType(clazz: Class<T>, id: String): JsonRpc2Request {
    val simpleName = clazz.simpleName
    return JsonRpc2Request(method = simpleName, id = id)
}
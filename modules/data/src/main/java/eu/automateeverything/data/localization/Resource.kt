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

package eu.automateeverything.data.localization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.stream.Collectors

@Serializable
data class ResourceMap(
    var EN: String,
    var PL: String
)


@Serializable
data class Resource(val englishValue: String, val polishValue: String) {
    private val _values = ResourceMap(englishValue, polishValue)

    constructor(map: ResourceMap) :
        this(map.EN, map.PL)

    fun getValue(language: Language): String {
        return when (language) {
            Language.EN -> _values.EN
            Language.PL -> _values.PL
        }
    }

    fun split(delimiter: String?): List<Resource> {
        if (isUniResource()) {
            val split = _values.EN
                .split(delimiter!!).toTypedArray()
            return Arrays
                .stream(split)
                .map { uniValue: String -> createUniResource(uniValue) }
                .collect(Collectors.toList())
        }

        return listOf(this)
    }

    private fun isUniResource(): Boolean {
        return _values.EN == _values.PL
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val resource = other as Resource
        return _values == resource._values
    }

    override fun hashCode(): Int {
        return Objects.hash(_values)
    }

    fun serialize(): String {
        return Json.encodeToString(_values)
    }

    companion object {
        fun createUniResource(uniValue: String): Resource {
            return Resource(uniValue, uniValue)
        }

        fun deserialize(json: String): Resource {
            val resourceMap =  Json.decodeFromString<ResourceMap>(json)
            return Resource(resourceMap)
        }
    }
}
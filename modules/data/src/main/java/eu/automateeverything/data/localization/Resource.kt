/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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
import java.util.Hashtable
import java.util.Arrays
import java.util.stream.Collectors
import java.util.Objects

@Serializable
class ResourceMap : Hashtable<Language, String>()

class Resource(englishValue: String, polishValue: String) {
    private val _values = ResourceMap()

    fun getValue(language: Language): String? {
        return _values[language]
    }

    fun split(delimiter: String?): List<Resource> {
        if (isUniResource()) {
            val split = _values[Language.EN]!!
                .split(delimiter!!).toTypedArray()
            return Arrays
                .stream(split)
                .map { uniValue: String -> createUniResource(uniValue) }
                .collect(Collectors.toList())
        }

        return listOf(this)
    }

    private fun isUniResource(): Boolean {
        return _values[Language.EN] == _values[Language.PL]
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

    init {
        _values[Language.EN] = englishValue
        _values[Language.PL] = polishValue
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
            return Resource(resourceMap[Language.EN]!!, resourceMap[Language.PL]!!)
        }
    }
}
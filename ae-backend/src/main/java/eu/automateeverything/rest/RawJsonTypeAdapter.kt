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

package eu.automateeverything.rest

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.data.localization.Language
import kotlin.Throws
import java.io.IOException

class RawJsonTypeAdapter(private val language: Language) : TypeAdapter<RawJson>() {
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): RawJson {
        throw IOException("Not implemented, RawJson can only be passed one-way server -> client")
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, input: RawJson) {
        val outputJson = input.templateFunction.invoke(language)
        out.jsonValue(outputJson)
    }
}
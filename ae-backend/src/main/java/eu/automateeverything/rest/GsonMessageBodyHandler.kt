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

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.data.localization.Language
import eu.automateeverything.data.localization.Resource
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.MultivaluedMap
import jakarta.ws.rs.ext.MessageBodyReader
import jakarta.ws.rs.ext.MessageBodyWriter
import java.io.*
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class GsonMessageBodyHandler : MessageBodyWriter<Any?>, MessageBodyReader<Any> {
    private val gsons = HashMap<Language, Gson>()

    private fun createGson(language: Language): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Resource::class.java, ResourceGsonTypeAdapter(language))
            .registerTypeAdapter(Class::class.java, ClassGsonTypeAdapter())
            .registerTypeAdapter(RawJson::class.java, RawJsonTypeAdapter(language))
            .setFieldNamingStrategy { f: Field -> f.name.replace("_".toRegex(), "") }
            .create()
    }

    @Context
    var requestHeaders: HttpHeaders? = null
    override fun isReadable(
        type: Class<*>?, genericType: Type,
        annotations: Array<Annotation>, mediaType: MediaType
    ): Boolean {
        return true
    }

    override fun readFrom(
        type: Class<Any>, genericType: Type,
        annotations: Array<Annotation>, mediaType: MediaType,
        httpHeaders: MultivaluedMap<String, String>, entityStream: InputStream
    ): Any {
        var streamReader: InputStreamReader? = null
        return try {
            streamReader = InputStreamReader(entityStream, StandardCharsets.UTF_8)
            val jsonType: Type = if (type == genericType) {
                type
            } else {
                genericType
            }
            gsons[Language.EN]!!
                .fromJson(streamReader, jsonType)
        } finally {
            try {
                streamReader!!.close()
            } catch (ignored: IOException) {
            }
        }
    }

    override fun isWriteable(
        type: Class<*>?, genericType: Type,
        annotations: Array<Annotation>, mediaType: MediaType
    ): Boolean {
        return true
    }

    override fun getSize(
        `object`: Any?, type: Class<*>?, genericType: Type,
        annotations: Array<Annotation>, mediaType: MediaType
    ): Long {
        return -1
    }

    @Throws(IOException::class, WebApplicationException::class)
    override fun writeTo(
        `object`: Any?, type: Class<*>, genericType: Type,
        annotations: Array<Annotation>, mediaType: MediaType,
        httpHeaders: MultivaluedMap<String, Any>,
        entityStream: OutputStream
    ) {
        val language = matchLanguage()
        val writer = OutputStreamWriter(entityStream, StandardCharsets.UTF_8)
        writer.use {
            val jsonType: Type = if (type == genericType) {
                type
            } else {
                genericType
            }
            gsons[language]!!.toJson(`object`, jsonType, it)
        }
    }

    private fun matchLanguage(): Language {
        if (requestHeaders!!.acceptableLanguages.size > 0) {
            val firstLocale = requestHeaders!!.acceptableLanguages[0]
            for (language in Language.values()) {
                if (language.name.equals(firstLocale.language, ignoreCase = true)) {
                    return language
                }
            }
        }

        //fallback
        return Language.EN
    }

    init {
        for (language in Language.values()) {
            gsons[language] = createGson(language)
        }
    }
}
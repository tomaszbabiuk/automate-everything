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
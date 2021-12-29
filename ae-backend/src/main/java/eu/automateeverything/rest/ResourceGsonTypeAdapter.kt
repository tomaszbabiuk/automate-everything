package eu.automateeverything.rest

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import eu.automateeverything.data.localization.Language
import eu.automateeverything.data.localization.Resource
import java.lang.UnsupportedOperationException
import kotlin.Throws
import java.io.IOException

class ResourceGsonTypeAdapter(private val _language: Language) : TypeAdapter<Resource>() {
    override fun read(`in`: JsonReader): Resource {
        throw UnsupportedOperationException()
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, resource: Resource?) {
        if (resource == null) {
            out.nullValue()
        } else {
            out.value(resource.getValue(_language))
        }
    }
}
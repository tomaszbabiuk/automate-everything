package eu.automateeverything.rest

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.Throws
import java.io.IOException
import java.lang.ClassNotFoundException

class ClassGsonTypeAdapter : TypeAdapter<Class<*>>() {
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Class<*> {
        val clazz = `in`.nextString()
        return try {
            Class.forName(clazz)
        } catch (e: ClassNotFoundException) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, clazz: Class<*>) {
        out.value(clazz.simpleName)
    }
}
package eu.geekhome.data.localization

import java.util.Hashtable
import java.util.Arrays
import java.util.stream.Collectors
import java.util.Objects

class Resource(englishValue: String, polishValue: String) {
    private val _values: Hashtable<Language, String> = Hashtable()

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

    companion object {
        fun createUniResource(uniValue: String): Resource {
            return Resource(uniValue, uniValue)
        }
    }

    init {
        _values[Language.EN] = englishValue
        _values[Language.PL] = polishValue
    }
}
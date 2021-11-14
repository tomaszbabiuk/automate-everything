package eu.automateeverything.timeplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.hardware.PortValue

class Datestamp(var value: Double) : PortValue {
    override fun toFormattedString(): Resource {
        return Resource.createUniResource(value.toString())
    }

    override fun asInteger(): Int {
        return value.toInt()
    }

    override fun asDouble(): Double {
        return value
    }
}
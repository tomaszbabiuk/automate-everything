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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.math.BigDecimal

class Relay(var value: BigDecimal) : PortValue {

    constructor(fromBoolean: Boolean) : this(
        if (fromBoolean) {
            BigDecimal.ONE
        } else {
            BigDecimal.ZERO
        }
    )

    private val on = Resource("On", "Wł")
    private val off = Resource("Off", "Wył")

    override fun toFormattedString(): Resource {
        return if (value == BigDecimal.ONE) on else off
    }

    override fun asDecimal(): BigDecimal {
        return value
    }

    override fun equals(other: Any?): Boolean {
        return (other is Relay) && other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    companion object {
        val ON = Relay(BigDecimal.ONE)
        val OFF = Relay(BigDecimal.ZERO)
    }
}
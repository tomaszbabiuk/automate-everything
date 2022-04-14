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
import java.math.BigDecimal

interface Port<V: PortValue> {
    val id: String

    val valueClazz: Class<V>

    val canRead: Boolean
        get() = this is InputPort<V>

    val canWrite: Boolean
        get() = this is OutputPort<V>

    fun tryRead() : V? {
        return (this as InputPort<V>).read()
    }

    val lastSeenTimestamp: Long
        get() = 0

    val sleepInterval: Long
        get() = 0
}

interface InputPort<V : PortValue> : Port<V> {
    fun read() : V
}

interface OutputPort<V : PortValue> : InputPort<V> {
    fun write(value : V)
    val requestedValue : V?
    fun reset()

    fun write(value: BigDecimal) {
        val newValue = PortValueBuilder.buildFromDecimal(valueClazz, value) as V
        write(newValue)
    }
}
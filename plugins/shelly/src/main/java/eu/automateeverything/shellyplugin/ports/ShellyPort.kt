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

package eu.automateeverything.shellyplugin.ports

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.hardware.*
import java.util.*

open class ShellyPort<V: PortValue>(
    override val id : String,
    override val valueClazz: Class<V>,
    val sleepInterval: Long
) : Connectible, Port<V> {
    final override var connectionValidUntil: Long = 0

    init {
        connectionValidUntil = Calendar.getInstance().timeInMillis + sleepInterval
    }
}


abstract class ShellyInputPort<V: PortValue>(
    id : String,
    valueClazz: Class<V>,
    sleepInterval: Long) : ShellyPort<V>(id, valueClazz, sleepInterval), InputPort<V> {

    abstract val readTopic: String
    abstract fun setValueFromMqttPayload(payload: String)
}

abstract class ShellyOutputPort<V: PortValue>(
    id : String,
    valueClazz: Class<V>,
    sleepInterval: Long) : ShellyInputPort<V>(id, valueClazz, sleepInterval), OutputPort<V> {

    abstract val writeTopic: String
    abstract fun getExecutePayload(): String?
}
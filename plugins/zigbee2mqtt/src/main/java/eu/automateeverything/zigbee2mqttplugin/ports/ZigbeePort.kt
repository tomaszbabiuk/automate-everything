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

package eu.automateeverything.zigbee2mqttplugin.ports

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.domain.hardware.Connectible
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.zigbee2mqttplugin.data.UpdatePayload
import java.util.*

abstract class ZigbeePort<V: PortValue>(
    override val id : String,
    override val valueClazz: Class<V>,
    val readTopic: String,
    val sleepInterval: Long
) : Connectible, Port<V> {
    final override var connectionValidUntil: Long = 0
    var isValueVerified = false
    init {
        connectionValidUntil = Calendar.getInstance().timeInMillis + sleepInterval
    }

    abstract fun tryUpdate(payload: UpdatePayload) : Boolean
}
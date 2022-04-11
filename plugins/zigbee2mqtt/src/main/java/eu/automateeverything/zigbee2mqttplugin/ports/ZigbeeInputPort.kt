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
import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.zigbee2mqttplugin.data.UpdatePayload

abstract class ZigbeeInputPort<V: PortValue>(
    id : String,
    valueClazz: Class<V>,
    readTopic: String,
    initialValue: V,
    sleepInterval: Long,
    lastSeenTimestamp: Long
) : ZigbeePort<V>(id, valueClazz, readTopic, sleepInterval, lastSeenTimestamp), InputPort<V> {

    var value = initialValue

    override fun read(): V {
        return value
    }

    override fun tryUpdate(payload: UpdatePayload): Boolean {
        val updated = tryUpdateInternal(payload)
        if (updated) {
            isValueVerified = tryUpdateInternal(payload)
        }

        return updated
    }

    abstract fun tryUpdateInternal(payload: UpdatePayload): Boolean
}


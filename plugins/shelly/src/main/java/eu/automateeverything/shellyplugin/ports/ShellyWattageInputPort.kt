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

import eu.automateeverything.domain.hardware.Wattage
import java.math.BigDecimal

enum class TopicSource {
    Light,
    Relay
}

class ShellyWattageInputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long,
    topicSource: TopicSource,
) : ShellyInputPort<Wattage>(id, Wattage::class.java, sleepInterval) {

    private val value = Wattage(BigDecimal.ZERO)

    override val readTopic =
        if (topicSource == TopicSource.Relay) {
            "shellies/$shellyId/relay/$channel/power"
        } else {
            "shellies/$shellyId/light/$channel/power"
        }

    override fun read(): Wattage {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal()
        value.value = valueParsed
    }
}
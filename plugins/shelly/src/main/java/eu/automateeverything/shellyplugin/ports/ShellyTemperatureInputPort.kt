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

import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.shellyplugin.TemperatureBriefDto
import java.math.BigDecimal

class ShellyTemperatureInputPort(
    id: String,
    shellyId: String,
    sleepInterval: Long)
    : ShellyInputPort<Temperature>(id, Temperature::class.java, sleepInterval) {

    private val value = Temperature(BigDecimal.ZERO)
    override val readTopic = "shellies/$shellyId/sensor/temperature"

    override fun read(): Temperature {
        return value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val valueParsed = payload.toBigDecimal() + 273.15.toBigDecimal()
        value.value = valueParsed
    }

    fun setValueFromTemperatureResponse(temperatureBrief: TemperatureBriefDto) {
        value.value = temperatureBrief.tC + 273.15.toBigDecimal()
    }
}
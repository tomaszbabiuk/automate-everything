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

import eu.automateeverything.domain.automation.blocks.CelsiusToKelvinValueConverter
import eu.automateeverything.domain.automation.blocks.FahrenheitToKelvinValueConverter
import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.zigbee2mqttplugin.data.UpdatePayload
import java.math.BigDecimal

class ZigbeeTemperatureInputPort(
    id: String,
    readTopic: String,
    unit: String,
    sleepInterval: Long,
    lastSeenTimestamp: Long
) : ZigbeeInputPort<Temperature>(id, Temperature::class.java, readTopic, Temperature(BigDecimal.ZERO), sleepInterval, lastSeenTimestamp) {

    private val isCelsius = unit.lowercase().contains("c")
    private val isFahrenheit = unit.lowercase().contains("f")
    private val celsiusToKConverter = CelsiusToKelvinValueConverter()
    private val fahrenheitToKConverter = FahrenheitToKelvinValueConverter()

    override fun tryUpdateInternal(payload: UpdatePayload): Boolean {
        if (payload.temperature != null) {
            val valueInK = if (isCelsius) {
                celsiusToKConverter.convert(BigDecimal(payload.temperature))
            } else if (isFahrenheit) {
                fahrenheitToKConverter.convert(BigDecimal(payload.temperature))
            } else {
                BigDecimal(payload.temperature)
            }
            value = Temperature(valueInK)
            return true
        }

        return false
    }
}
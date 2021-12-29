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

import com.google.gson.Gson
import eu.automateeverything.domain.hardware.PowerLevel
import eu.automateeverything.shellyplugin.LightBriefDto
import eu.automateeverything.shellyplugin.LightSetDto
import java.math.BigDecimal

class ShellyPowerLevelOutputPort(
    id: String,
    shellyId: String,
    channel: Int,
    sleepInterval: Long
) : ShellyOutputPort<PowerLevel>(id, PowerLevel::class.java, sleepInterval) {

    private val gson = Gson()
    private val readValue = PowerLevel(BigDecimal.ZERO)
    override var requestedValue : PowerLevel? = null
    override val readTopic = "shellies/$shellyId/light/$channel/status"
    override val writeTopic = "shellies/$shellyId/light/$channel/set"

    override fun read(): PowerLevel {
        return readValue
    }

    override fun write(value: PowerLevel) {
        requestedValue = value
    }

    override fun setValueFromMqttPayload(payload: String) {
        val response: LightBriefDto = gson.fromJson(payload, LightBriefDto::class.java)
        setValueFromLightResponse(response)
    }

    fun setValueFromLightResponse(lightResponse: LightBriefDto) {
        val valueInPercent = calculateBrightness(lightResponse)
        readValue.value = valueInPercent.toBigDecimal()
    }

    private fun calculateBrightness(lightResponse: LightBriefDto): Int {
        val isOn = lightResponse.ison
        return if (isOn) lightResponse.brightness else 0
    }

    override fun getExecutePayload(): String? {
        if (requestedValue == null) {
            return null
        }

        val response: LightSetDto = if (requestedValue!!.value == BigDecimal.ZERO) {
            LightSetDto("off", 0)
        } else {
            LightSetDto("on", requestedValue!!.value.toInt())
        }

        requestedValue = null

        return gson.toJson(response)
    }


    override fun reset() {
        requestedValue = null
    }
}
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

package eu.automateeverything.shellyplugin

import eu.automateeverything.domain.hardware.*
import eu.automateeverything.shellyplugin.ports.*
import java.util.*
import kotlin.collections.ArrayList

class ShellyPortFactory {

    fun constructPorts(
        shellyId: String,
        idBuilder: PortIdBuilder,
        statusResponse: ShellyStatusResponse,
        settingsResponse: ShellySettingsResponse,
        now: Calendar
    ) : List<ShellyInputPort<*>> {
        val result = ArrayList<ShellyInputPort<*>>()
        var sleepInterval = 60000L
        if (settingsResponse.sleep_mode != null) {
            sleepInterval += settingsResponse.sleep_mode.period * settingsResponse.sleep_mode.unit.minutes * 60000L
        }
        if (statusResponse.relays != null) {
            for (i in statusResponse.relays.indices) {
                val relayResponse = statusResponse.relays[i]
                result.add(constructRelayOutputPort(idBuilder, shellyId, i, relayResponse, sleepInterval, now.timeInMillis))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructWattageReadPort(idBuilder, shellyId, i, sleepInterval, now.timeInMillis))
                }
            }
        }

        if (statusResponse.lights != null) {
            for (i in statusResponse.lights.indices) {
                val lightResponse = statusResponse.lights[i]
                result.add(constructPowerLevelReadWritePort(idBuilder, shellyId, i, lightResponse, sleepInterval, now.timeInMillis))
            }

            if (statusResponse.meters != null) {
                for (i in statusResponse.meters.indices) {
                    result.add(constructWattageReadPort(idBuilder, shellyId, i, sleepInterval, now.timeInMillis))
                }
            }
        }

        if (statusResponse.inputs != null) {
            for (i in statusResponse.inputs.indices) {
                result.add(constructBooleanReadPort(idBuilder, shellyId, i, statusResponse.inputs[i], sleepInterval, now.timeInMillis))
            }
        }

        if (statusResponse.tmp != null) {
            result.add(constructTemperatureReadPort(idBuilder, shellyId, statusResponse.tmp, sleepInterval, now.timeInMillis))
        }

        if (statusResponse.hum != null) {
            result.add(constructHumidityReadPort(idBuilder, shellyId, statusResponse.hum, sleepInterval, now.timeInMillis))
        }

        if (statusResponse.bat != null) {
            result.add(constructBatteryReadPort(idBuilder, shellyId, statusResponse.bat, sleepInterval, now.timeInMillis))
        }

        if (statusResponse.lux != null) {
            result.add(constructLuminosityReadPort(idBuilder, shellyId, statusResponse.lux, sleepInterval, now.timeInMillis))
        }

        if (statusResponse.sensor != null) {
            result.add(constructStateReadPort(idBuilder, shellyId, statusResponse.sensor, sleepInterval, now.timeInMillis))
        }

        if (statusResponse.accel != null) {
            result.add(constructVibrationReadPort(idBuilder, shellyId, statusResponse.accel, sleepInterval, now.timeInMillis))
        }

        return result
    }

    private fun constructBooleanReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        inputBriefDto: InputBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "I")
        val port = ShellyBinaryInputPort(id, shellyId, channel, sleepInterval, lastSeenTimestamp)
        port.setValueFromInputResponse(inputBriefDto)

        return port
    }

    private fun constructBatteryReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        batteryBrief: BatteryBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "B")
        val port = ShellyBatteryInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromBatteryResponse(batteryBrief)

        return port
    }

    private fun constructHumidityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        humidityBrief: HumidityBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "H")
        val port = ShellyHumidityInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromHumidityResponse(humidityBrief)
        return port
    }

    private fun constructLuminosityReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        luminosityBrief: LuminosityBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "L")
        val port = ShellyLuminosityInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromLuminosityResponse(luminosityBrief)
        return port
    }

    private fun constructStateReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        stateBrief: StateBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, "state", "I")
        val port = ShellyStateInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromStateResponse(stateBrief)
        return port
    }

    private fun constructVibrationReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        accelBrief: AccelBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, "vibration", "I")
        val port = ShellyVibrationInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromAccelResponse(accelBrief)
        return port
    }

    private fun constructTemperatureReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        temperatureBrief: TemperatureBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, 0.toString(), "T")
        val port = ShellyTemperatureInputPort(id, shellyId, sleepInterval, lastSeenTimestamp)
        port.setValueFromTemperatureResponse(temperatureBrief)
        return port
    }

    private fun constructRelayOutputPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        relayResponse: RelayResponseDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "R")
        val port = ShellyRelayOutputPort(id, shellyId, channel, sleepInterval, lastSeenTimestamp)
        port.setValueFromRelayResponse(relayResponse)
        return port
    }

    private fun constructPowerLevelReadWritePort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        lightResponse: LightBriefDto,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ): ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "L")
        val port = ShellyPowerLevelOutputPort(id, shellyId, channel, sleepInterval, lastSeenTimestamp)
        port.setValueFromLightResponse(lightResponse)
        return port
    }

    private fun constructWattageReadPort(
        idBuilder: PortIdBuilder,
        shellyId: String,
        channel: Int,
        sleepInterval: Long,
        lastSeenTimestamp: Long
    ) : ShellyInputPort<*> {
        val id = idBuilder.buildPortId(shellyId, channel.toString(), "W")
        return ShellyWattageInputPort(id, shellyId, channel, sleepInterval, lastSeenTimestamp)
    }
}
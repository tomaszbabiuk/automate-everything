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

import java.math.BigDecimal

data class ShellyStatusResponse(
    val relays: List<RelayResponseDto>?,
    val meters: List<MeterBriefDto>?,
    val lights: List<LightBriefDto>?,
    val tmp: TemperatureBriefDto?,
    val hum: HumidityBriefDto?,
    val bat: BatteryBriefDto?,
    val lux: LuminosityBriefDto?,
    val sensor: StateBriefDto?,
    val accel: AccelBriefDto?,
    val inputs: List<InputBriefDto>?,
    val adcs: AdcBriefDto?,
    val mac: String,
    val ext_temperature: Map<String, ExtraTemperatureBrief>?,
    val ext_humidity: HumidityBriefDto?
)

data class ExtraTemperatureBrief(
    val hwID: String,
    val tC: Double
)

data class AdcBriefDto(
    val voltage: Double
)

data class RelayResponseDto(
    val ison: Boolean
)

data class MeterBriefDto(
    val power: Double
)

data class LightBriefDto(
    val ison: Boolean,
    val mode: String,
    val brightness: Int
)

data class TemperatureBriefDto(
    val tC: BigDecimal
)

data class HumidityBriefDto(
    val value: BigDecimal
)

data class LuminosityBriefDto(
    val value: BigDecimal
)

data class BatteryBriefDto(
    val value: BigDecimal,
    val voltage: Double
)

data class InputBriefDto(
    val input: Int
)

data class StateBriefDto(
    val state: String
)

data class AccelBriefDto(
    val vibration: Int
)

data class LightSetDto(
    val turn: String,
    var brightness: Int
)

data class ShellySettingsResponse(
    var device: DeviceBriefDto,
    val mqtt: MqttBriefDto,
    val cloud: CloudBriefDto,
    val sleep_mode: SleepModeDto?
)

enum class SleepModeUnit(val minutes: Int) {
    h(60),m(1);
}

data class SleepModeDto(
    val period: Int,
    val unit: SleepModeUnit,
)

data class CloudBriefDto(
    val enabled: Boolean
)

data class DeviceBriefDto(
    val type: String,
    val hostname: String,
    val num_outputs: Int = 0,
    val num_meters: Int = 0
)

data class MqttBriefDto(
    val enable: Boolean,
    val server: String?
)
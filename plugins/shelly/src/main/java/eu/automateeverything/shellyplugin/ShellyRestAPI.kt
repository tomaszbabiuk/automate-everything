package eu.automateeverything.shellyplugin

data class ShellyStatusResponse(
    val relays: List<RelayResponseDto>?,
    val meters: List<MeterBriefDto>?,
    val lights: List<LightBriefDto>?,
    val tmp: TemperatureBriefDto?,
    val hum: HumidityBriefDto?,
    val bat: BatteryBriefDto?,
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
    val tC: Double
)

data class HumidityBriefDto(
    val value: Double
)

data class BatteryBriefDto(
    val value: Double,
    val voltage: Double
)

data class InputBriefDto(
    val input:Int
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
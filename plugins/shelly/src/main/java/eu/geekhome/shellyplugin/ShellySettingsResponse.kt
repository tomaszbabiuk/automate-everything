package eu.geekhome.shellyplugin

data class ShellySettingsResponse(

    var device: DeviceBriefDto,
    val relays: List<RelayResponseDto>?,
    val meters: List<MeterBriefDto>?,
    val lights: List<LightBriefDto>?,
    val mqtt: MqttBriefDto,
    val cloud: CloudBriefDto
)
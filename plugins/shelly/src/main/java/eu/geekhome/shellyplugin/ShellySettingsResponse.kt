package eu.geekhome.shellyplugin

import com.google.gson.annotations.SerializedName


data class ShellySettingsResponse(

    @SerializedName("device")
    var device: DeviceBriefDto,

    @SerializedName("relays")
    val relays: List<RelayResponseDto>?,

    @SerializedName("meters")
    val meters: List<MeterBriefDto>?,

    @SerializedName("lights")
    val lights: List<LightBriefDto>?,

    @SerializedName("mqtt")
    val mqtt: MqttBriefDto,

    @SerializedName("cloud")
    val cloud: CloudBriefDto
)
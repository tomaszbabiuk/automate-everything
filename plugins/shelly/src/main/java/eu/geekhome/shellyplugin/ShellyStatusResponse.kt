package eu.geekhome.shellyplugin


data class ShellyStatusResponse(
    val relays: List<RelayResponseDto>?,
    val meters: List<MeterBriefDto>?,
    val lights: List<LightBriefDto>?,
    val tmp: TemperatureBriefDto?,
    val hum: HumidityBriefDto?,
    val bat: BatteryBriefDto?,
    val inputs: List<InputBriefFDto>?,
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

data class InputBriefFDto(
    val input:Int
)

/*
{
  "wifi_sta": {
    "connected": true,
    "ssid": "giga18405",
    "ip": "192.168.1.111",
    "rssi": -56
  },
  "cloud": {
    "enabled": false,
    "connected": false
  },
  "mqtt": {
    "connected": false
  },
  "time": "21:38",
  "unixtime": 1614721138,
  "serial": 1,
  "has_update": false,
  "mac": "98F4ABF38705",
  "cfg_changed_cnt": 0,
  "actions_stats": {
    "skipped": 0
  },
  "is_valid": true,
  "tmp": {
    "value": 26.12,
    "units": "C",
    "tC": 26.12,
    "tF": 79.03,
    "is_valid": true
  },
  "hum": {
    "value": 49,
    "is_valid": true
  },
  "bat": {
    "value": 86,
    "voltage": 2.91
  },
  "act_reasons": [
    "poweron"
  ],


      "inputs": [
      {
        "input": 0
      },
      {
        "input": 0
      }
    ],
    "adcs": [
      {
        "voltage": 11.31
      }
    ],
    "ext_sensors": {
      "temperature_unit": "C"
    },
    "ext_temperature": [
      "0": {
        "hwID": "287169120c0000d7",
        "tC": 24.5,
        "tF": 76.1
      }
    ],
    "ext_humidity": {}


  "connect_retries": 0,
  "update": {
    "status": "unknown",
    "has_update": false,
    "new_version": "",
    "old_version": "20201124-091711/v1.9.0@57ac4ad8"
  },
  "ram_total": 51224,
  "ram_free": 38876,
  "fs_size": 233681,
  "fs_free": 151353,
  "uptime": 9
}
 */
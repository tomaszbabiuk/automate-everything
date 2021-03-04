package eu.geekhome.shellyplugin

data class ShellySettingsResponse(
    var device: DeviceBriefDto,
    val mqtt: MqttBriefDto,
    val cloud: CloudBriefDto,
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

/*
{
  "wifi_sta": {
    "connected": true,
    "ssid": "giga18405",
    "ip": "192.168.1.102",
    "rssi": -71
  },
  "cloud": {
    "enabled": false,
    "connected": false
  },
  "mqtt": {
    "connected": true
  },
  "time": "21:12",
  "unixtime": 1614715972,
  "serial": 1676,
  "has_update": false,
  "mac": "CC50E3376CC2",
  "cfg_changed_cnt": 1,
  "actions_stats": {
    "skipped": 0
  },
  "relays": [
    {
      "ison": true,
      "has_timer": false,
      "timer_started": 0,
      "timer_duration": 0,
      "timer_remaining": 0,
      "overpower": false,
      "source": "mqtt"
    }
  ],
  "meters": [
    {
      "power": 0,
      "overpower": 0,
      "is_valid": true,
      "timestamp": 1614719572,
      "counters": [
        0,
        0.42,
        0.553
      ],
      "total": 4341
    }
  ],
  "temperature": 30.37,
  "overtemperature": false,
  "tmp": {
    "tC": 30.37,
    "tF": 86.67,
    "is_valid": true
  },
  "update": {
    "status": "idle",
    "has_update": false,
    "new_version": "20210115-103659/v1.9.4@e2732e05",
    "old_version": "20210115-103659/v1.9.4@e2732e05",
    "beta_version": "20210209-101711/v1.10.0-rc2-g0cf9ee3-heads/v1.10.0-rc2"
  },
  "ram_total": 51040,
  "ram_free": 38152,
  "fs_size": 233681,
  "fs_free": 162146,
  "uptime": 28344
}
 */
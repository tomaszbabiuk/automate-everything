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

package eu.automateeverything.zigbee2mqttplugin.data

/*
{
   "data":{
      "definition":{
         "description":"Temperature and humidity sensor",
         "exposes":[
            {
               "access":1,
               "description":"Remaining battery in %",
               "name":"battery",
               "property":"battery",
               "type":"numeric",
               "unit":"%",
               "value_max":100,
               "value_min":0,
               "values":[
                  "single",
                  "double",
                  "long"
               ]
            },
            {
               "access":1,
               "description":"Measured temperature value",
               "name":"temperature",
               "property":"temperature",
               "type":"numeric",
               "unit":"°C"
            },
            {
               "access":1,
               "description":"Measured relative humidity",
               "name":"humidity",
               "property":"humidity",
               "type":"numeric",
               "unit":"%"
            },
            {
               "access":1,
               "description":"Voltage of the battery in millivolts",
               "name":"voltage",
               "property":"voltage",
               "type":"numeric",
               "unit":"mV"
            },
            {
               "access":1,
               "description":"Link quality (signal strength)",
               "name":"linkquality",
               "property":"linkquality",
               "type":"numeric",
               "unit":"lqi",
               "value_max":255,
               "value_min":0
            }
         ],
         "model":"SNZB-02",
         "options":[
            {
               "access":2,
               "description":"Number of digits after decimal point for temperature, takes into effect on next report of device.",
               "name":"temperature_precision",
               "property":"temperature_precision",
               "type":"numeric",
               "value_max":3,
               "value_min":0
            },
            {
               "access":2,
               "description":"Calibrates the temperature value (absolute offset), takes into effect on next report of device.",
               "name":"temperature_calibration",
               "property":"temperature_calibration",
               "type":"numeric"
            },
            {
               "access":2,
               "description":"Number of digits after decimal point for humidity, takes into effect on next report of device.",
               "name":"humidity_precision",
               "property":"humidity_precision",
               "type":"numeric",
               "value_max":3,
               "value_min":0
            },
            {
               "access":2,
               "description":"Calibrates the humidity value (absolute offset), takes into effect on next report of device.",
               "name":"humidity_calibration",
               "property":"humidity_calibration",
               "type":"numeric"
            }
         ],
         "supports_ota":false,
         "vendor":"SONOFF"
      },
      "friendly_name":"0x00124b0025033adc",
      "ieee_address":"0x00124b0025033adc",
      "status":"successful",
      "supported":true
   },
   "type":"device_interview"
}
 */

data class InterviewDetails(
    val definition: Definition,
    val ieee_address: String,
    val status: String,
    val supported: Boolean
)
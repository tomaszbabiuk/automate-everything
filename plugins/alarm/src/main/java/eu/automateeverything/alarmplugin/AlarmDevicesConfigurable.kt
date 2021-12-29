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

package eu.automateeverything.alarmplugin

import eu.automateeverything.domain.configurable.CategoryConfigurable
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.devices.DevicesConfigurable
import org.pf4j.Extension

@Extension
class AlarmDevicesConfigurable : CategoryConfigurable() {

    override val parent: Class<out Configurable>
        get() = DevicesConfigurable::class.java

    override val titleRes: Resource
        get() = R.configurable_alarmdevices_title

    override val descriptionRes: Resource
        get() = R.configurable_alarmdevices_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" clip-rule="evenodd">
             <defs>
              <style type="text/css">.fil0 {fill:black}</style>
             </defs>
             <g class="layer">
              <title>thief By Pamerat, SG from Noun Project</title>
              <g id="svg_1">
               <path class="fil0" d="m10.72103,28.98817l4.67223,0l0,2.42317l9.258,-9.11751l21.99226,21.48793l-4.21113,4.32881l-3.68159,-3.59873l0,22.65389l-15.83947,0l0,-10.45878l-6.9417,0l0,10.45878l-5.2486,0l0,-22.68271l-3.66958,3.61554l-4.23395,-4.30359l7.90353,-7.78345l0,-7.02335zm34.20178,1.32206l12.32839,-5.8646l1.28964,2.73177l-12.3296,5.8658l-1.28844,-2.73297zm-9.05747,-7.60573l5.5464,-12.47609l2.75579,1.2296l-5.5488,12.47489l-2.75339,-1.2284zm5.44673,2.1542l10.45998,-8.77409l1.93926,2.3163l-10.46118,8.77409l-1.93806,-2.3163zm3.702,61.30816l34.00485,0l-3.26852,-14.12116l-4.02741,-4.02741l-1.51178,7.63815c-0.37945,1.93926 -2.17821,3.2397 -4.10666,3.03437l-13.47033,-0.9246c-4.95802,-0.32901 -4.45489,-7.8663 0.50193,-7.53729l10.30268,0.70726l1.88762,-9.54379c0.99424,-5.60764 8.1677,-12.29958 14.58586,-17.71989c2.23105,-1.88402 8.014,4.04662 7.88311,6.02791l0.49832,5.2378l3.80527,-3.46545c2.9359,-2.67173 7.00294,1.79877 4.06704,4.4705l-7.4064,6.74357c-1.63186,1.58383 -4.42247,0.85736 -5.02406,-1.3905l-1.28243,-4.79591l-5.26181,6.28488l4.66503,4.66623c0.58358,0.58358 0.93541,1.31005 1.05909,2.06654l3.68399,15.91752c0.49352,2.1566 -0.72527,4.33601 -3.05358,4.33601c-12.84353,0 -25.68826,0 -38.53179,0l0,-3.60474zm34.82138,-44.0998c-11.57911,-1.72192 -18.31308,-0.05283 -22.01267,2.29229c-8.91818,5.65327 0.19813,16.33419 7.68138,10.70014c6.36052,-4.7851 14.33129,-12.99243 14.33129,-12.99243zm10.29187,-6.97772c3.13283,0 5.67368,2.54205 5.67368,5.67368c0,3.13283 -2.54085,5.67368 -5.67368,5.67368c-3.13283,0 -5.67248,-2.54085 -5.67248,-5.67368c0,-3.13163 2.53965,-5.67368 5.67248,-5.67368zm-73.75903,6.46739l6.67513,0l0,6.67393l-6.67513,0l0,-6.67393zm10.67973,0l6.67393,0l0,6.67393l-6.67393,0l0,-6.67393z" fill="black" id="svg_2"/>
              </g>
             </g>
            </svg>
        """.trimIndent()
}
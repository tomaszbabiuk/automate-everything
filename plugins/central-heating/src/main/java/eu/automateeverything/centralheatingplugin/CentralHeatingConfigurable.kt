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

package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.devices.DevicesConfigurable
import eu.automateeverything.domain.configurable.CategoryConfigurable
import eu.automateeverything.domain.configurable.Configurable
import org.pf4j.Extension

@Extension
class CentralHeatingConfigurable : CategoryConfigurable() {

    override val parent: Class<out Configurable>
        get() = DevicesConfigurable::class.java

    override val titleRes: Resource
        get() = R.configurable_central_heating_title

    override val descriptionRes: Resource
        get() = R.configurable_central_heating_description

    override val iconRaw: String
        get() = """<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="100px" height="100px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
                <g>
                    <circle cx="50.159" cy="48.501" r="28.063"/>
                </g>
                <polygon points="11.728,41.943 11.728,55.058 5.16,48.523 "/>
                <polygon points="88.591,55.06 88.591,41.945 95.159,48.48 "/>
                <polygon points="18.347,71.039 27.62,80.312 18.354,80.336 "/>
                <polygon points="81.972,25.964 72.698,16.69 81.965,16.667 "/>
                <polygon points="72.697,80.313 81.971,71.04 81.994,80.306 "/>
                <polygon points="27.622,16.688 18.349,25.962 18.324,16.695 "/>
                <polygon points="56.718,10.07 43.604,10.07 50.138,3.501 "/>
                <polygon points="43.601,86.933 56.716,86.933 50.181,93.502 "/>
                </svg>""".trimIndent()
}
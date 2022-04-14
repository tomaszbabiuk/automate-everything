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

package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.domain.configurable.CategoryConfigurable
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.devices.DevicesConfigurable
import org.pf4j.Extension

@Extension
class ControllersConfigurable : CategoryConfigurable() {

    override val parent: Class<out Configurable>
        get() = DevicesConfigurable::class.java

    override val titleRes: Resource
        get() = R.configurable_controllers_title

    override val descriptionRes: Resource
        get() = R.configurable_controllers_description

    override val iconRaw: String
        get() = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="100px" height="100px" viewBox="0 0 100 100" style="enable-background:new 0 0 100 100;" xml:space="preserve">
                <path style="fill:#010101;" d="M100,25h-6.25V6.25C93.75,2.795,90.967,0,87.5,0c-3.442,0-6.25,2.795-6.25,6.25V25H75v12.5h6.25  v56.25c0,3.455,2.808,6.25,6.25,6.25c3.467,0,6.25-2.795,6.25-6.25V37.5H100V25z"/>
                <path style="fill:#010101;" d="M62.5,62.5h-6.25V6.25C56.25,2.795,53.467,0,50,0c-3.442,0-6.25,2.795-6.25,6.25V62.5H37.5V75h6.25  v18.75c0,3.455,2.808,6.25,6.25,6.25c3.467,0,6.25-2.795,6.25-6.25V75h6.25V62.5z"/>
                <path style="fill:#010101;" d="M25,37.5h-6.25V6.25C18.75,2.795,15.967,0,12.5,0C9.058,0,6.25,2.795,6.25,6.25V37.5H0V50h6.25v43.75  c0,3.455,2.808,6.25,6.25,6.25c3.467,0,6.25-2.795,6.25-6.25V50H25V37.5z"/>
            </svg>
        """.trimIndent()
}
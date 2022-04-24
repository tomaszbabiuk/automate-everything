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

package eu.automateeverything.crypto

import eu.automateeverything.data.fields.PortReference
import eu.automateeverything.data.fields.PortReferenceType
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.PortReferenceField
import eu.automateeverything.domain.configurable.RequiredStringValidator
import eu.automateeverything.domain.configurable.SinglePortDeviceConfigurable
import eu.automateeverything.domain.events.EventsBus
import eu.automateeverything.domain.hardware.PortFinder
import org.pf4j.Extension

@Extension
class TickerConfigurable(
    portFinder: PortFinder,
    eventsBus: EventsBus
) : SinglePortDeviceConfigurable<Ticker>(
    Ticker::class.java,
    eventsBus,
    PortReferenceField(
        FIELD_PORT,
        R.field_port_hint,
        PortReference(Ticker::class.java, PortReferenceType.Input),
        RequiredStringValidator()
    ),
    portFinder
) {
    override val hasAutomation: Boolean = false

    override val parent: Class<out Configurable>?
        get() = null

    override val addNewRes: Resource
        get() = R.configurable_ticker_add

    override val editRes: Resource
        get() = R.configurable_ticker_edit

    override val titleRes: Resource
        get() = R.configurable_ticker_title

    override val descriptionRes: Resource
        get() = R.configurable_ticker_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com" data-name="Layer 1">
             <g class="layer">
              <title>Layer 1</title>
              <g id="svg_3">
               <path d="m50,3.5a46.5,46.5 0 1 0 46.5,46.5a46.5527,46.5527 0 0 0 -46.5,-46.5zm0,86.8a40.3,40.3 0 1 1 40.3,-40.3a40.3465,40.3465 0 0 1 -40.3,40.3z" fill="black" id="svg_1"/>
               <path d="m68.6,40.7a12.4,12.4 0 0 0 -9.3,-11.9598l0,-3.5402a3.1,3.1 0 0 0 -6.2,0l0,3.1l-6.2,0l0,-3.1a3.1,3.1 0 0 0 -6.2,0l0,3.1l-9.3,0a3.1,3.1 0 0 0 0,6.2l3.1,0l0,31l-3.1,0a3.1,3.1 0 0 0 0,6.2l9.3,0l0,3.1a3.1,3.1 0 0 0 6.2,0l0,-3.1l6.2,0l0,3.1a3.1,3.1 0 0 0 6.2,0l0,-3.5402a12.3008,12.3008 0 0 0 5.0158,-21.2598a12.3225,12.3225 0 0 0 4.2842,-9.3zm-6.2,18.6a6.2,6.2 0 0 1 -6.2,6.2l-15.5,0l0,-12.4l15.5,0a6.2,6.2 0 0 1 6.2,6.2zm-21.7,-12.4l0,-12.4l15.5,0a6.2,6.2 0 0 1 0,12.4l-15.5,0z" fill="black" id="svg_2"/>
              </g>
             </g>
            </svg>
        """.trimIndent()


    companion object {
        const val FIELD_PORT = "portId"
    }

    override val blocksCategory: BlockCategory
        get() = CryptoBlockCategories.Crypto
}
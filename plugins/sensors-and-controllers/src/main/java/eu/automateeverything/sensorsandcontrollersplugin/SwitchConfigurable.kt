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

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.configurable.BinaryInputPortField
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.RequiredStringValidator
import eu.automateeverything.domain.configurable.SinglePortDeviceConfigurable
import eu.automateeverything.domain.events.EventsSink
import eu.automateeverything.domain.hardware.BinaryInput
import eu.automateeverything.domain.hardware.PortFinder
import org.pf4j.Extension

@Extension
class SwitchConfigurable(
    portFinder: PortFinder,
    eventsSink: EventsSink
) :
    SinglePortDeviceConfigurable<BinaryInput>(
        BinaryInput::class.java, eventsSink,
        BinaryInputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator()),
        portFinder
    ) {
    override val parent: Class<out Configurable>? = null

    override val addNewRes: Resource
        get() = R.configurable_switch_add

    override val editRes: Resource
        get() = R.configurable_switch_edit

    override val titleRes: Resource
        get() = R.configurable_switches_title

    override val descriptionRes: Resource
        get() = R.configurable_switches_description

    override val iconRaw: String
        get() = """
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1" id="Layer_1" x="0px" y="0px" width="70.667px" height="100px" viewBox="0 0 70.667 100" enable-background="new 0 0 70.667 100" xml:space="preserve">
                <path d="M64.814,100.125H6.106c-3.203,0-5.807-2.604-5.807-5.807V5.932c0-3.203,2.604-5.807,5.807-5.807h58.708  c3.202,0,5.807,2.604,5.807,5.807v88.386C70.621,97.521,68.017,100.125,64.814,100.125z M7.037,93.387h56.846V6.863H7.037V93.387z"/>
                <path d="M47.903,51.754c-0.151-0.299-0.275-0.817-0.275-1.152V29.538c0-0.335-0.274-0.61-0.61-0.61h-22.93  c-0.335,0-0.609,0.274-0.609,0.61v31.364c0,0.335,0.119,0.856,0.267,1.157l4.184,8.621c0.147,0.3,0.271,0.531,0.274,0.513  c0.005-0.02,0.031,0.009,0.06,0.065c0.028,0.055,0.324,0.101,0.659,0.101h20.015c0.336,0,0.717-0.252,0.849-0.562l3.318-7.812  c0.131-0.31,0.114-0.807-0.038-1.104L47.903,51.754z M33.188,59.273c-0.335,0-0.714-0.253-0.843-0.565l-2.669-6.447  c-0.128-0.311,0.041-0.564,0.376-0.564h12.536c0.336,0,0.738,0.243,0.893,0.54l3.41,6.496c0.155,0.298,0.009,0.541-0.326,0.541  H33.188z M41.049,34.039c0.335,0,1.469,1.595,1.85,3.743c0.61,3.429,0.52,9.797,0.52,9.81c0,0.012-0.274,0.021-0.609,0.021h-13.61  c-0.336,0-0.61-0.273-0.61-0.609V34.648c0-0.336,0.274-0.61,0.61-0.61H41.049z M32.277,67.631c-0.336,0-0.517-0.258-0.4-0.573  l1.138-3.129c0.117-0.317,0.483-0.575,0.819-0.575h14.062c0.335,0,0.502,0.253,0.369,0.561L46.91,67.07  c-0.133,0.308-0.514,0.561-0.85,0.561H32.277z"/>
            </svg>
        """.trimIndent()

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Triggers
}
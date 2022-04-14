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

import eu.automateeverything.domain.configurable.RequiredStringValidator
import eu.automateeverything.domain.configurable.WattageInputPortField
import eu.automateeverything.domain.hardware.Wattage
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.StateChangeReporter
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.configurable.SinglePortDeviceConfigurable
import eu.automateeverything.domain.hardware.PortFinder
import org.pf4j.Extension

@Extension
class WattmeterConfigurable(portFinder: PortFinder, stateChangeReporter: StateChangeReporter) :
    SinglePortDeviceConfigurable<Wattage>(
        Wattage::class.java, stateChangeReporter,
        WattageInputPortField(FIELD_PORT, R.field_port_hint, RequiredStringValidator()),
        portFinder
    ) {
    override val parent: Class<out Configurable?>
        get() = SensorsConfigurable::class.java

    override val addNewRes: Resource
        get() = R.configurable_wattmeter_add

    override val editRes: Resource
        get() = R.configurable_wattmeter_edit

    override val titleRes: Resource
        get() = R.configurable_wattmeter_title

    override val descriptionRes: Resource
        get() = R.configurable_wattmeter_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Voltmeter by Arafat Uddin from the Noun Project</title>
              <g id="svg_1">
               <g id="svg_2">
                <path d="m88.78824,18.35978l0,-1.99411c0,-1.58878 -1.287,-2.87578 -2.87578,-2.87578s-2.87578,1.287 -2.87578,2.87578l0,1.99411c-1.71896,0.9941 -2.87578,2.85211 -2.87578,4.97936l0,11.69099c0,2.12873 1.15682,3.98526 2.87578,4.97936l0,42.73578c0,3.65094 -2.97045,6.62139 -6.62139,6.62139l-32.91612,0c-3.65094,0 -6.62139,-2.97045 -6.62139,-6.62139l0,-6.57849l-5.75156,0l0,6.57849c0,6.82258 5.55037,12.37295 12.37295,12.37295l32.9176,0c6.82258,0 12.37295,-5.55037 12.37295,-12.37295l0,-42.7343c1.71896,-0.9941 2.87578,-2.85211 2.87578,-4.97936l0,-11.69099c-0.00148,-2.12873 -1.1583,-3.98674 -2.87726,-4.98084z" id="svg_3"/>
                <path d="m74.85166,18.35978l0,-1.99411c0,-1.58878 -1.287,-2.87578 -2.87578,-2.87578c-1.58878,0 -2.87578,1.287 -2.87578,2.87578l0,1.99411c-1.71896,0.9941 -2.87578,2.85211 -2.87578,4.97936l0,11.69099c0,2.12873 1.15682,3.98526 2.87578,4.97936l0,37.66027c0,0.18787 -0.15237,0.34024 -0.34024,0.34024l-17.26059,0c-0.18787,0 -0.34024,-0.15237 -0.34024,-0.34024l0,-1.50298l-5.75156,0l0,1.50298c0,3.35803 2.73229,6.0918 6.0918,6.0918l17.25911,0c3.35951,0 6.0918,-2.73229 6.0918,-6.0918l0,-37.65879c1.71896,-0.9941 2.87578,-2.85211 2.87578,-4.97936l0,-11.69099c0.00148,-2.12873 -1.15682,-3.98674 -2.8743,-4.98084z" id="svg_4"/>
               </g>
               <path d="m51.16348,4.88179l-34.89988,0c-4.37136,0 -7.9291,3.55626 -7.9291,7.9291l0,52.2078c0,4.37136 3.55626,7.9291 7.9291,7.9291l34.89988,0c4.37136,0 7.9291,-3.55626 7.9291,-7.9291l0,-52.2078c-0.00148,-4.37284 -3.55774,-7.9291 -7.9291,-7.9291zm2.09174,60.1369c0,1.15386 -0.93788,2.09174 -2.09174,2.09174l-34.89988,0c-1.15386,0 -2.09174,-0.93788 -2.09174,-2.09174l0,-52.2078c0,-1.15386 0.93788,-2.09174 2.09174,-2.09174l34.89988,0c1.15386,0 2.09174,0.93788 2.09174,2.09174l0,52.2078z" id="svg_5"/>
               <path d="m36.41476,41.28465l-9.11699,9.11699c-0.97339,0.97339 -0.97339,2.55329 0,3.52667c0.48669,0.48669 1.12575,0.73078 1.76334,0.73078c0.63758,0 1.27664,-0.24409 1.76334,-0.73078l9.11699,-9.11699c0.97339,-0.97339 0.97339,-2.55329 0,-3.52667c-0.97339,-0.97339 -2.55329,-0.97339 -3.52667,0z" id="svg_6"/>
               <path d="m47.84686,14.37452l-28.26813,0c-0.93344,0 -1.69381,0.81954 -1.69381,1.82547l0,45.55682c0,1.00593 0.76037,1.82547 1.69381,1.82547l28.26813,0c0.93492,0 1.69381,-0.81954 1.69381,-1.82547l0,-45.55682c0,-1.00741 -0.75889,-1.82547 -1.69381,-1.82547zm-11.07559,6.03854c0,-1.09469 0.88759,-1.98375 1.98375,-1.98375l4.26633,0c1.09469,0 1.98375,0.88759 1.98375,1.98375l0,0c0,1.09469 -0.88759,1.98375 -1.98375,1.98375l-4.26633,0c-1.09617,0 -1.98375,-0.88907 -1.98375,-1.98375l0,0zm0,6.81666c0,-1.09469 0.88759,-1.98375 1.98375,-1.98375l4.26633,0c1.09469,0 1.98375,0.88759 1.98375,1.98375l0,0c0,1.09469 -0.88759,1.98375 -1.98375,1.98375l-4.26633,0c-1.09617,0 -1.98375,-0.88759 -1.98375,-1.98375l0,0zm-14.34931,-6.81666c0,-1.09469 0.88759,-1.98375 1.98375,-1.98375l7.98383,0c1.09469,0 1.98375,0.88759 1.98375,1.98375l0,0c0,1.09469 -0.88759,1.98375 -1.98375,1.98375l-7.98383,0c-1.09617,0 -1.98375,-0.88907 -1.98375,-1.98375l0,0zm0,6.81666c0,-1.09469 0.88759,-1.98375 1.98375,-1.98375l7.98383,0c1.09469,0 1.98375,0.88759 1.98375,1.98375l0,0c0,1.09469 -0.88759,1.98375 -1.98375,1.98375l-7.98383,0c-1.09617,0 -1.98375,-0.88759 -1.98375,-1.98375l0,0zm11.1969,32.1528c-6.50305,0 -11.77531,-5.27226 -11.77531,-11.77531s5.27226,-11.77531 11.77531,-11.77531s11.77531,5.27226 11.77531,11.77531s-5.27226,11.77531 -11.77531,11.77531z" id="svg_7"/>
              </g>
             </g>
            </svg>
        """.trimIndent()

    override val blocksCategory: BlockCategory
        get() = CommonBlockCategories.Wattage
}
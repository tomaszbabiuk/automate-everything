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

package eu.automateeverything.actions

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.CategoryConfigurable
import eu.automateeverything.domain.configurable.Configurable
import org.pf4j.Extension

@Extension
class ActionsConfigurable : CategoryConfigurable() {

    override val parent: Class<out Configurable?>?
        get() = null

    override val titleRes: Resource
        get() = R.configurable_actions_title

    override val descriptionRes: Resource
        get() = R.configurable_actions_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com">
             <title>a</title>
             <g class="layer">
              <title>script By Berkah Icon from The Noun Project</title>
              <g data-name="21 Sinopsis Movie" id="svg_1">
               <path d="m92.1875,78.125l-11.25,0l0,-64.6875a8.44594,8.44594 0 0 0 -8.4375,-8.4375l-59.0625,0a8.44594,8.44594 0 0 0 -8.4375,8.4375l0,5.625a2.8125,2.8125 0 0 0 2.8125,2.8125l14.0625,0l0,64.6875a8.44594,8.44594 0 0 0 8.4375,8.4375l56.25,0a8.44594,8.44594 0 0 0 8.4375,-8.4375l0,-5.625a2.8125,2.8125 0 0 0 -2.8125,-2.8125zm-81.5625,-61.875l0,-2.8125a2.8125,2.8125 0 0 1 2.8125,-2.8125l5.625,0a2.8125,2.8125 0 0 1 2.8125,2.8125l0,2.8125l-11.25,0zm16.875,70.3125l0,-73.125a8.37563,8.37563 0 0 0 -0.1125,-1.30922c-0.01125,-0.07313 -0.01969,-0.14063 -0.03375,-0.22219a8.18438,8.18438 0 0 0 -0.31078,-1.18406c-0.00984,-0.03094 -0.01406,-0.06609 -0.02531,-0.09703l45.48234,0a2.8125,2.8125 0 0 1 2.8125,2.8125l0,64.6875l-28.125,0a2.8125,2.8125 0 0 0 -2.8125,2.8125l0,5.625a2.8125,2.8125 0 0 1 -2.8125,2.8125l-11.25,0a2.8125,2.8125 0 0 1 -2.8125,-2.8125zm61.875,0a2.8125,2.8125 0 0 1 -2.8125,2.8125l-37.04484,0c0.01125,-0.03094 0.01547,-0.06609 0.02531,-0.09703a8.18438,8.18438 0 0 0 0.31078,-1.18406c0.01406,-0.07453 0.0225,-0.14906 0.03375,-0.22219a8.37563,8.37563 0 0 0 0.1125,-1.30922l0,-2.8125l39.375,0l0,2.8125z" id="svg_2"/>
               <path d="m42.98984,38.38437a2.8125,2.8125 0 0 0 2.83219,-0.03516l14.0625,-8.4375a2.8125,2.8125 0 0 0 0,-4.82344l-14.0625,-8.4375a2.8125,2.8125 0 0 0 -4.25953,2.41172l0,16.875a2.8125,2.8125 0 0 0 1.42734,2.44687zm4.19766,-14.355l5.78391,3.47062l-5.78391,3.47062l0,-6.94125z" id="svg_3"/>
               <path d="m44.375,50l14.0625,0a2.8125,2.8125 0 0 0 0,-5.625l-14.0625,0a2.8125,2.8125 0 0 0 0,5.625z" id="svg_4"/>
               <path d="m35.9375,61.25l30.9375,0a2.8125,2.8125 0 0 0 0,-5.625l-30.9375,0a2.8125,2.8125 0 0 0 0,5.625z" id="svg_5"/>
               <path d="m69.6875,66.875a2.8125,2.8125 0 0 0 -2.8125,-2.8125l-30.9375,0a2.8125,2.8125 0 0 0 0,5.625l30.9375,0a2.8125,2.8125 0 0 0 2.8125,-2.8125z" id="svg_6"/>
              </g>
             </g>
            </svg>
        """.trimIndent()
}
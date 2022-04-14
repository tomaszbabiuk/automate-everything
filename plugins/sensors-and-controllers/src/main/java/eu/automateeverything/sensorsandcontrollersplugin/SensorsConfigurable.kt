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
class SensorsConfigurable : CategoryConfigurable() {

    override val parent: Class<out Configurable>
        get() = DevicesConfigurable::class.java

    override val titleRes: Resource
        get() = R.configurable_sensors_title

    override val descriptionRes: Resource
        get() = R.configurable_sensors_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg">
             <g class="layer">
              <title>Created by Eucalyp</title>
              <g id="svg_1">
               <circle cx="44.13793" cy="58.7931" id="svg_2" r="1.46552"/>
               <path d="m44.71681,38.68328c-0.08793,-0.24328 -0.31948,-0.40741 -0.57888,-0.40741s-0.49095,0.16414 -0.57888,0.40741l-6.32371,17.71078c-0.28138,0.78845 -0.425,1.61207 -0.425,2.44741c0,4.01405 3.26517,7.27922 7.27776,7.27922c4.11224,0 7.37741,-3.26517 7.37741,-7.27776c0,-0.83681 -0.14216,-1.66043 -0.425,-2.44741l-6.32371,-17.71224zm-0.57888,24.50638c-2.42397,0 -4.39655,-1.97259 -4.39655,-4.39655s1.97259,-4.39655 4.39655,-4.39655s4.39655,1.97259 4.39655,4.39655s-1.97259,4.39655 -4.39655,4.39655z" id="svg_3"/>
               <path d="m79.31035,33.87931c7.2719,0 13.18966,-5.91776 13.18966,-13.18966s-5.91776,-13.18966 -13.18966,-13.18966s-13.18966,5.91776 -13.18966,13.18966s5.91776,13.18966 13.18966,13.18966zm-6.37207,-13.78612l6.37207,-11.15112l6.37207,11.14966c0.62431,1.09474 0.95552,2.34043 0.95552,3.60078c0,4.00086 -3.25491,7.25578 -7.25578,7.25578c-4.14448,0 -7.3994,-3.25491 -7.3994,-7.25578c0,-1.26034 0.33121,-2.50603 0.95552,-3.59931z" id="svg_4"/>
               <path d="m79.23854,28.01724c2.52802,0 4.46836,-1.94034 4.46836,-4.32474c0,-0.75181 -0.19638,-1.49483 -0.57009,-2.14552l-3.82647,-6.69595l-3.82647,6.69595c-0.37371,0.65216 -0.57009,1.39371 -0.57009,2.14552c0,2.3844 1.94034,4.32474 4.32474,4.32474z" id="svg_5"/>
               <path d="m62.79104,66.58086l3.39853,3.39853c1.63698,-2.295 3.85578,-4.14009 6.44388,-5.32422c-5.56164,-2.54414 -9.44379,-8.14974 -9.44379,-14.65517s3.88216,-12.11103 9.44379,-14.65517c-2.5881,-1.18414 -4.8069,-3.02922 -6.44388,-5.32422l-3.39853,3.39853l-2.07224,-2.07224l3.95103,-3.95103c-0.0806,-0.1744 -0.16707,-0.34293 -0.24181,-0.52026c-5.24655,-4.60612 -11.86922,-7.25285 -18.82457,-7.58405l0,5.79466l-2.93103,0l0,-5.78733c-7.37888,0.35026 -14.0719,3.29741 -19.20121,7.96362l4.08586,4.08586l-2.07224,2.07224l-4.08586,-4.08586c-4.66474,5.12784 -7.61336,11.82086 -7.96216,19.19974l5.78733,0l0,2.93103l-5.78733,0c0.35026,7.37888 3.29741,14.0719 7.96362,19.20121l4.08586,-4.08586l2.07224,2.07224l-4.08586,4.08586c5.12931,4.66621 11.82233,7.61336 19.20121,7.96362l0,-5.78879l2.93103,0l0,5.79466c6.95388,-0.33121 13.57802,-2.97793 18.82457,-7.58552c0.07328,-0.17733 0.16121,-0.34586 0.24181,-0.52026l-3.95103,-3.95103l2.07078,-2.07078zm-18.60328,2.47086c-5.72871,0 -10.30845,-4.57974 -10.30845,-10.20879c0,-1.17241 0.20078,-2.32871 0.595,-3.43371l6.32517,-17.71078c0.50267,-1.40836 1.84362,-2.35362 3.33845,-2.35362s2.83724,0.94672 3.33845,2.35216l6.32517,17.71078c0.39422,1.10647 0.595,2.26129 0.595,3.43517c0,5.62905 -4.57974,10.20879 -10.20879,10.20879z" id="svg_6"/>
               <path d="m63.27026,77.71293c-5.60121,3.87043 -12.24586,5.99397 -19.13233,5.99397c-18.58569,0 -33.7069,-15.12121 -33.7069,-33.7069s15.12121,-33.7069 33.7069,-33.7069c6.88793,0 13.53259,2.12353 19.13233,5.99397c-0.05129,-0.52612 -0.0806,-1.0581 -0.0806,-1.59741c0,-0.64043 0.04836,-1.26914 0.12164,-1.89052c-5.75948,-3.54802 -12.36164,-5.43707 -19.17336,-5.43707c-20.20216,0 -36.63793,16.43578 -36.63793,36.63793s16.43578,36.63793 36.63793,36.63793c6.81172,0 13.41388,-1.88905 19.1719,-5.43707c-0.07181,-0.62138 -0.12017,-1.25009 -0.12017,-1.89052c0,-0.53931 0.02931,-1.07129 0.0806,-1.59741z" id="svg_7"/>
               <path d="m79.31035,66.12069c-7.2719,0 -13.18966,5.91776 -13.18966,13.18966s5.91776,13.18966 13.18966,13.18966s13.18966,-5.91776 13.18966,-13.18966s-5.91776,-13.18966 -13.18966,-13.18966zm4.39655,23.44828c-2.42397,0 -4.39655,-1.97259 -4.39655,-4.39655l2.93103,0c0,0.8075 0.65802,1.46552 1.46552,1.46552s1.46552,-0.65802 1.46552,-1.46552s-0.65802,-1.46552 -1.46552,-1.46552l-14.65517,0l0,-2.93103l14.65517,0c2.42397,0 4.39655,1.97259 4.39655,4.39655s-1.97259,4.39655 -4.39655,4.39655zm0,-11.72414l-14.65517,0l0,-2.93103l14.65517,0c0.8075,0 1.46552,-0.65802 1.46552,-1.46552s-0.65802,-1.46552 -1.46552,-1.46552s-1.46552,0.65802 -1.46552,1.46552l-2.93103,0c0,-2.42397 1.97259,-4.39655 4.39655,-4.39655s4.39655,1.97259 4.39655,4.39655s-1.97259,4.39655 -4.39655,4.39655z" id="svg_8"/>
               <path d="m80.77586,50l-0.07181,0c0.0469,0.47043 0.07181,0.95845 0.07181,1.46552l-2.93103,0c0,-4.41267 -2.41664,-7.00224 -3.90853,-8.18052c-0.79431,1.50948 -1.95353,4.15034 -1.95353,6.715c0,3.56121 3.76638,7.32759 7.32759,7.32759s7.32759,-3.76638 7.32759,-7.32759c0,-2.59397 -1.17095,-5.24216 -1.96672,-6.74285c-1.49043,1.09328 -3.89534,3.37509 -3.89534,6.74285z" id="svg_9"/>
               <path d="m79.31035,36.81035c-7.2719,0 -13.18966,5.91776 -13.18966,13.18966s5.91776,13.18966 13.18966,13.18966s13.18966,-5.91776 13.18966,-13.18966s-5.91776,-13.18966 -13.18966,-13.18966zm0,23.44828c-5.17767,0 -10.25862,-5.08095 -10.25862,-10.25862c0,-4.78491 3.04828,-9.41155 3.17724,-9.60647l0.7181,-1.07569l1.15776,0.57888c0.18319,0.09233 3.15526,1.6194 5.06922,5.10293c1.89052,-3.33845 5.15129,-5.00621 5.34328,-5.10293l1.15776,-0.57888l0.7181,1.07569c0.1275,0.19491 3.17578,4.82155 3.17578,9.60647c0,5.17767 -5.08095,10.25862 -10.25862,10.25862z" id="svg_10"/>
              </g>
             </g>
            </svg>
        """.trimIndent()
}
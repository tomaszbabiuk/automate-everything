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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue

interface PortFinder {
    fun <T : PortValue> listAllOfAnyType(valueClass: Class<T>) : List<Port<T>>
    fun <T : PortValue> listAllOfInputType(valueClass: Class<T>) : List<InputPort<T>>
    fun <T : PortValue> listAllOfOutputType(valueClass: Class<T>) : List<OutputPort<T>>
    fun <T : PortValue> searchForAnyPort(valueClazz: Class<T>, id:String): Port<*>
    fun <T : PortValue> searchForInputPort(valueClazz: Class<T>, id:String): InputPort<T>
    fun <T : PortValue> searchForOutputPort(valueClazz: Class<T>, id:String): OutputPort<T>
    fun checkNewPorts(): Boolean
    fun clearNewPortsFlag()
}
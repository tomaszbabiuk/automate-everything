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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.AdapterState

enum class DiscoveryMode {
    Startup,
    Manual
}

interface HardwareAdapter<T : Port<*>> {

    val ports: HashMap<String, T>
    fun clearNewPortsFlag()
    fun hasNewPorts(): Boolean
    val adapterId: String
    suspend fun discover(mode: DiscoveryMode)

    var state: AdapterState
    fun executePendingChanges()

    fun stop()
    fun start()

    var lastDiscoveryTime: Long
    var lastError: Throwable?
}
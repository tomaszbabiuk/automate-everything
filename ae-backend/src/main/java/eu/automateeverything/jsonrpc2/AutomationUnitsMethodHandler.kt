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

package eu.automateeverything.jsonrpc2

import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.interop.MethodHandler
import eu.automateeverything.interop.SubscriptionHandler
import eu.automateeverything.mappers.AutomationUnitDtoMapper
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.encodeToByteArray

class AutomationUnitsMethodHandler(private val automationConductor: AutomationConductor,
                                   private val mapper: AutomationUnitDtoMapper
) : MethodHandler {
        override fun matches(method: String): Boolean {
            return method == "GetAutomationUnits"
        }

        override fun handle(format: BinaryFormat, params: ByteArray?, subscriptions: MutableList<SubscriptionHandler>): ByteArray {
            val units = automationConductor
                .automationUnitsCache
                .values
                .map {
                    mapper.map(it.second, it.first)
                }

            return format.encodeToByteArray(units)
        }
}
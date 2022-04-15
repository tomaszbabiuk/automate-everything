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

package eu.automateeverything.domain.automation

import eu.automateeverything.data.localization.Resource
import kotlinx.coroutines.*
import java.util.*

class DelayAutomationNode(
    override val next: StatementNode?,
    private val doNode: StatementNode?,
    private val seconds: Int) : StatementNodeBase() {

    private var scope: CoroutineScope? = null

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (scope != null) {
            scope!!.cancel("Clearing old scope of delay automation node")
        }

        if (doNode != null) {
            scope = CoroutineScope(Dispatchers.IO)
            scope!!.launch {
                var secondsTillEnd = seconds
                while (isActive && secondsTillEnd > 0) {
                    delay(1000)
                    secondsTillEnd--
                    doNode.modifyNote("TILL_END", Resource.createUniResource(secondsTillEnd.toString()))
                }
                doNode.removeNote("TILL_END")

                if (isActive) {
                    doNode.process(Calendar.getInstance(), firstLoop)
                }
            }
        }
    }
}

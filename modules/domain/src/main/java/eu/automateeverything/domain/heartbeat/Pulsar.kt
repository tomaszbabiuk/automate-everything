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

package eu.automateeverything.domain.heartbeat

import eu.automateeverything.domain.WithStartStopScope
import eu.automateeverything.domain.automation.AutomationConductor
import eu.automateeverything.domain.events.EventsBus
import eu.automateeverything.domain.inbox.Inbox
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class Pulsar(
    val eventsBus: EventsBus,
    val inbox: Inbox,
    private val automationConductor: AutomationConductor
    ) : WithStartStopScope<Void?>() {

    override fun start(params: Void?) {
        super.start(params)
        startStopScope.launch {
            while (isActive) {
                sendHeartbeatEvent()
                delay(10000)
            }
        }
    }

    private fun sendHeartbeatEvent() {
        val timestamp = Calendar.getInstance().timeInMillis
        val isAutomationEnabled = automationConductor.isEnabled()

        eventsBus.broadcastHeartbeatEvent(timestamp, inbox.unreadMessagesCount, inbox.totalMessagesCount, isAutomationEnabled)
    }
}
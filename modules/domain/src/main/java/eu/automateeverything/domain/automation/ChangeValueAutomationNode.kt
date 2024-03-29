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

import eu.automateeverything.data.hardware.PortValue
import eu.automateeverything.data.localization.Resource
import java.util.*

class ChangeValueAutomationNode(
    private val value: ValueNode?,
    private val deviceUnit: ControllerAutomationUnit<in PortValue>,
    override val next: StatementNode?
) : StatementNodeBase() {

    override fun process(now: Calendar, firstLoop: Boolean) {
        if (value != null) {
            val controlValue = value.getValue(now)
            if (controlValue != null) {
                deviceUnit.control(controlValue)
            }
        }

        next?.process(now, firstLoop)
    }

    override fun modifyNote(noteId: String, note: Resource) {
        super.modifyNote(noteId, note)
        deviceUnit.modifyNote(noteId, note)
    }

    override fun removeNote(noteId: String) {
        super.removeNote(noteId)
        deviceUnit.removeNote(noteId)
    }
}
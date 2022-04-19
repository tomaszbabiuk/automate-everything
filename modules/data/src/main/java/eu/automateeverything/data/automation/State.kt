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

package eu.automateeverything.data.automation

import eu.automateeverything.data.localization.Resource
import kotlinx.serialization.Serializable

@Serializable
class State(
    val id: String,
    val name: Resource,
    val action: Resource?,
    val type: StateType,
    val isSignaled: Boolean = false) {

    companion object {
        fun buildReadOnlyState(id: String,
                          name: Resource,
                          isSignaled: Boolean = false): State {
            return State(id, name, null, StateType.ReadOnly, isSignaled)
        }

        fun buildControlState(id: String,
                              name: Resource,
                              action: Resource,
                              isSignaled: Boolean = false): State {
            return State(id, name, action, StateType.Control, isSignaled)
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}
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

import eu.automateeverything.data.automation.NextStatesDto
import eu.automateeverything.data.localization.Resource
import kotlinx.serialization.Serializable


class EvaluationResult<T> (
    val interfaceValue: Resource,
    val value: T? = null,
    val isSignaled: Boolean = false,
    var descriptions: List<Resource> = ArrayList(),
    val error: Exception? = null,
    val nextStates: NextStatesDto? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EvaluationResult<*>

        if (interfaceValue != other.interfaceValue) return false
        if (value != other.value) return false
        if (isSignaled != other.isSignaled) return false
        if (descriptions != other.descriptions) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = interfaceValue.hashCode()
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + isSignaled.hashCode()
        result = 31 * result + descriptions.hashCode()
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}
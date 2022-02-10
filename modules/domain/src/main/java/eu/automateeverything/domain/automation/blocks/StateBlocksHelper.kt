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

package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.localization.Language
import java.lang.StringBuilder

fun buildStateOption(state: State, language: Language): String {
    return """["${state.name.getValue(language)}", "${state.id}"]"""
}

fun buildStateOptions(states: Map<String, State>, language: Language): String {
    val result = StringBuilder()
    states
        .forEach{
            if (result.isNotEmpty()) {
                result.append(",")
            }
            result.append(buildStateOption(it.value, language))
        }

    return result.toString()
}

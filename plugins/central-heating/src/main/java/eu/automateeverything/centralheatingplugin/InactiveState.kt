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

package eu.automateeverything.centralheatingplugin

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.*

enum class InactiveState(val raw: String, private val translation: Resource) {
    NO("no", R.value_no),
    NC("nc", R.value_nc);

    fun toPair() : Pair<String, Resource> {
        return Pair(raw, translation)
    }
}

class ContactTypeField(name: String, hint: Resource, vararg validators: Validator<InactiveState>) :
    FieldDefinition<InactiveState>(
        FieldType.SingleOptionEnumeration, name, hint, 0, InactiveState.NC, InactiveState::class.java,
        ContactTypeFieldBuilder(), null, mapOf(InactiveState.NO.toPair(), InactiveState.NC.toPair()), *validators) {
}

class ContactTypeFieldBuilder : FieldBuilder<InactiveState> {
    override fun fromPersistableString(value: String?): InactiveState {
        if (value == null || value.isEmpty()) {
            throw ParsingFieldException(InactiveState::class.java, value)
        }

        return InactiveState.values().first { it.raw == value }
    }

    override fun toPersistableString(value: InactiveState): String {
        return value.raw
    }
}
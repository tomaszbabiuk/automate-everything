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

package eu.automateeverything.domain.configurable

import eu.automateeverything.domain.R
import java.util.HashMap

abstract class NameDescriptionConfigurable : ConfigurableWithFields {

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap()
            result[FIELD_NAME] = nameField
            result[FIELD_DESCRIPTION] = descriptionField
            return result
        }

    protected val nameField = StringField(
        FIELD_NAME, R.field_name_hint, 50, "",
        RequiredStringValidator(), MaxStringLengthValidator(50)
    )

    protected val descriptionField = StringField(
        FIELD_DESCRIPTION, R.field_description_hint, 200, "",
        MaxStringLengthValidator(200)
    )

    companion object {
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
    }
}
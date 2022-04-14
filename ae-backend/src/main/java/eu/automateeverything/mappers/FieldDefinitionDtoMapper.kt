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

package eu.automateeverything.mappers

import eu.automateeverything.data.fields.*
import eu.automateeverything.domain.configurable.FieldDefinition

class FieldDefinitionDtoMapper {
    fun map(field: FieldDefinition<*>): FieldDefinitionDto {
        return FieldDefinitionDto(
            field.type,
            field.name,
            field.hint,
            field.maxSize,
            field.initialValueAsString(),
            if (field.reference != null) {
                if (field.reference!! is PortReference) {
                    ReferenceDto(field.reference!!.clazz.name,
                        (field.reference!! as PortReference).type.toString())
                } else if (field.reference!! is InstanceReference) {
                    ReferenceDto(field.reference!!.clazz.name,
                        (field.reference!! as InstanceReference).type.toString())
                } else {
                    null
                }
            } else {
                null
            },
            field.values
        )
    }
}
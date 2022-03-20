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

import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.data.settings.SettingGroupDto
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.configurable.*
import java.util.stream.Collectors
import jakarta.inject.Inject

class SettingGroupDtoMapper @Inject constructor(
    private val fieldDefinitionDtoMapper: FieldDefinitionDtoMapper
) {
    @Throws(MappingException::class)
    fun map(category: SettingGroup): SettingGroupDto {
        val fields: List<FieldDefinitionDto>? = category
            .fieldDefinitions
            .values
            .stream()
            .map { field: FieldDefinition<*> -> fieldDefinitionDtoMapper.map(field) }
            .collect(Collectors.toList())

        return SettingGroupDto(
            category.javaClass.name,
            category.titleRes,
            category.descriptionRes,
            fields
        )
    }
}
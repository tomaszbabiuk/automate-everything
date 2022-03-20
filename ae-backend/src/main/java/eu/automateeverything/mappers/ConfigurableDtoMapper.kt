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

import eu.automateeverything.data.configurables.ConfigurableDto
import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.rest.MappingException
import eu.automateeverything.domain.configurable.*
import eu.automateeverything.data.localization.Resource
import java.util.stream.Collectors
import jakarta.inject.Inject

class ConfigurableDtoMapper @Inject constructor(
    private val fieldDefinitionDtoMapper: FieldDefinitionDtoMapper
) {
    @Throws(MappingException::class)
    fun map(configurable: Configurable): ConfigurableDto {
        var fields: List<FieldDefinitionDto>? = null
        var addNewRes: Resource? = null
        var editRes: Resource? = null
        if (configurable is ConfigurableWithFields) {
            fields = configurable
                .fieldDefinitions
                .values
                .stream()
                .map { field: FieldDefinition<*> -> fieldDefinitionDtoMapper.map(field) }
                .collect(Collectors.toList())
            addNewRes = configurable.addNewRes
            editRes = configurable.editRes
        }

        return ConfigurableDto(
            configurable.titleRes,
            configurable.descriptionRes,
            configurable.javaClass.name,
            if (configurable.parent != null) configurable.parent!!.name else null,
            getValueClazz(configurable)?.name,
            fields,
            addNewRes,
            editRes,
            configurable.iconRaw,
            configurable.hasAutomation,
            configurable.editableIcon,
            configurable.taggable,
            configurable.generable
        )
    }

    private fun getValueClazz(configurable: Configurable): Class<out Any?>? {
        if (configurable is DeviceConfigurable<*>) {
            return configurable.valueClazz
        }

        return null
    }
}
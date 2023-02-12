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

package eu.automateeverything.data.configurables

import eu.automateeverything.data.fields.FieldDefinitionDto
import eu.automateeverything.data.localization.Resource
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurableDto(
    val titleRes: Resource,
    val descriptionRes: Resource,
    val clazz: String,
    val parentClazz: String?,
    val valueClazz: String?,
    val fields: List<FieldDefinitionDto>?,
    val addNewRes: Resource?,
    val editRes: Resource?,
    val iconRaw: String,
    val hasAutomation: Boolean,
    val editableIcon: Boolean,
    val taggable: Boolean,
    val generated : Boolean
)
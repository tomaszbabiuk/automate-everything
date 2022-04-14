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

package eu.automateeverything.domain.configurable

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.fields.Reference
import eu.automateeverything.data.localization.Resource

abstract class FieldDefinition<T> protected constructor(
    val type: FieldType,
    val name: String,
    val hint: Resource,
    val maxSize: Int,
    private val initialValue: T,
    val valueClazz: Class<T>,
    val builder: FieldBuilder<T>,
    val reference: Reference? = null,
    val values: Map<String, Resource>? = null,
    private vararg val validators: Validator<T>
) {
    fun validate(valueAsString: String?, fields: Map<String, String?>): FieldValidationResult {
        var isFieldValid = true
        val failingReasons: MutableList<Resource> = ArrayList()
        val value = builder.fromPersistableString(valueAsString)
        for (validator in validators) {
            val isValid = validator.validate(value, fields)
            if (!isValid) {
                isFieldValid = false
                failingReasons.add(validator.reason)
            }
        }
        return FieldValidationResult(isFieldValid, failingReasons)
    }

    fun initialValueAsString() : String {
        return builder.toPersistableString(initialValue)
    }
}
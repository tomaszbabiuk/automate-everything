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

import eu.automateeverything.data.fields.FieldType
import eu.automateeverything.data.localization.Resource

class DurationField(
    name: String,
    hint: Resource,
    initialValue: Duration,
    vararg validators: Validator<Duration>
) : FieldDefinition<Duration>(
    FieldType.Duration, name, hint, 0, initialValue,
    Duration::class.java, DurationFieldBuilder(), null, null, *validators)
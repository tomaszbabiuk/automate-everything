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

import java.lang.NumberFormatException

class LongFieldBuilder : FieldBuilder<Long?> {
    override fun fromPersistableString(value: String?): Long {
        return if (value == null) {
            0L
        } else try {
            value.toLong()
        } catch (nfe: NumberFormatException) {
            0L
        }
    }

    override fun toPersistableString(value: Long?): String {
        return value.toString()
    }
}
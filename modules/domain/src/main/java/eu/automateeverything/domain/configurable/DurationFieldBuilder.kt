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

class DurationFieldBuilder : FieldBuilder<Duration> {
    override fun fromPersistableString(value: String?): Duration {
        if (value == null || value.isEmpty()) {
            return Duration(0)
        }

        val segments = value.split(":")
        val hours = segments[0].toInt()
        val minutes = segments[1].toInt()
        val seconds = segments[2].toInt()
        val totalSeconds = hours * 3600 + minutes * 60 + seconds
        return Duration(totalSeconds)
    }

    override fun toPersistableString(value: Duration): String {
        val seconds = value.seconds % 60
        val totalMinutes = value.seconds / 60
        val minutes = totalMinutes % 60
        val hours: Int = totalMinutes / 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
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

package eu.automateeverything.crypto.coingeckoapi

import java.time.Duration
import java.util.*

enum class ChartRange(val duration: Duration, val calendarField: Int) {
    Week(Duration.ofDays(7), Calendar.WEEK_OF_YEAR),
    Day(Duration.ofDays(1), Calendar.DAY_OF_YEAR),
    Hour(Duration.ofHours(1), Calendar.HOUR_OF_DAY)
}
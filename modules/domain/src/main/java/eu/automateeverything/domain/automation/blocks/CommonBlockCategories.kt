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

package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R

enum class CommonBlockCategories(
    override val categoryName: Resource,
    override val color: Int
) : BlockCategory {
    Triggers(R.category_triggers, 300),
    Logic(R.category_logic, 150),
    Temperature(R.category_temperature, 240),
    Wattage(R.category_wattage, 330),
    Humidity(R.category_humidity, 120),
    Luminosity(R.category_luminosity, 127),
    State(R.category_state, 210),
    ThisObject(R.category_this_object, 270),
    PowerLevel(R.category_power_level, 33),
    Conditions(R.category_triggers_conditions, 0);
}
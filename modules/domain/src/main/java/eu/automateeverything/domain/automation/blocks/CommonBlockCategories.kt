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
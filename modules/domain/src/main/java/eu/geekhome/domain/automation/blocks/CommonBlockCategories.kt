package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.R

enum class CommonBlockCategories(
    override val categoryName: Resource,
    override val color: Int
) : BlockCategory {
    Triggers(R.category_triggers, 315),
    Logic(R.category_logic, 120),
    Temperature(R.category_temperature, 160),
    Wattage(R.category_wattage, 65),
    Humidity(R.category_humidity, 210),
    State(R.category_state, 100),
    ThisDevice(R.category_this_device, 230),
    Conditions(R.category_triggers_conditions, 120);
}
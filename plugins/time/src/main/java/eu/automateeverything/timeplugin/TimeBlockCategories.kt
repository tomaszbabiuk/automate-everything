package eu.automateeverything.timeplugin

import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.blocks.BlockCategory

enum class TimeBlockCategories(
    override val categoryName: Resource,
    override val color: Int
) : BlockCategory {
    SecondOfDay(R.category_time, 215),
    DayOfYear(R.category_date, 205),
}
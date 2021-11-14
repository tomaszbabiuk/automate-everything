package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.blocks.ComparisonBlockFactory
import eu.automateeverything.domain.automation.blocks.EquationBlockFactory

class TimeComparisonBlockFactory :
    ComparisonBlockFactory<Timestamp>(Timestamp::class.java, TimeBlockCategories.Time)

class TimeEquationBlockFactory :
    EquationBlockFactory<Timestamp>(Timestamp::class.java, TimeBlockCategories.Time)

class DateComparisonBlockFactory :
    ComparisonBlockFactory<Datestamp>(Datestamp::class.java, TimeBlockCategories.Date)

class DateEquationBlockFactory :
    EquationBlockFactory<Datestamp>(Datestamp::class.java, TimeBlockCategories.Date)
package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.*
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.hardware.Humidity
import eu.automateeverything.domain.hardware.PowerLevel
import eu.automateeverything.domain.hardware.Temperature
import eu.automateeverything.domain.hardware.Wattage
import org.pf4j.Extension

@Extension
class CoreBlocksCollector() : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        return listOf (
            //temperature
            ComparisonBlockFactory(Temperature::class.java, CommonBlockCategories.Temperature),
            EquationBlockFactory(Temperature::class.java, CommonBlockCategories.Temperature),
            TemperatureValueInCBlockFactory(CommonBlockCategories.Temperature.color),
            TemperatureValueInKBlockFactory(CommonBlockCategories.Temperature.color),
            TemperatureValueInFBlockFactory(CommonBlockCategories.Temperature.color),

            //humidity
            ComparisonBlockFactory(Humidity::class.java, CommonBlockCategories.Humidity),
            EquationBlockFactory(Humidity::class.java, CommonBlockCategories.Humidity),
            HumidityValueBlockFactory(CommonBlockCategories.Humidity.color),

            //wattage
            ComparisonBlockFactory(Wattage::class.java, CommonBlockCategories.Wattage),
            EquationBlockFactory(Wattage::class.java, CommonBlockCategories.Wattage),
            WattageValueBlockFactory(CommonBlockCategories.Wattage.color),

            //power level
            ComparisonBlockFactory(PowerLevel::class.java, CommonBlockCategories.PowerLevel),
            EquationBlockFactory(PowerLevel::class.java, CommonBlockCategories.PowerLevel),
            PowerLevelValueBlockFactory(CommonBlockCategories.PowerLevel.color)
        )
    }
}
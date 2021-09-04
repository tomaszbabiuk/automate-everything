package eu.automateeverything.coreplugin

import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.automation.blocks.*
import eu.geekhome.domain.configurable.Configurable
import eu.geekhome.domain.hardware.Humidity
import eu.geekhome.domain.hardware.Temperature
import eu.geekhome.domain.hardware.Wattage
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
            WattageValueBlockFactory(CommonBlockCategories.Wattage.color)
        )
    }
}
package eu.automateeverything.sensorsandcontrollersplugin

import eu.automateeverything.domain.automation.BlockFactory
import eu.automateeverything.domain.automation.blocks.*
import eu.automateeverything.domain.configurable.Configurable
import eu.automateeverything.domain.hardware.*
import org.pf4j.Extension

@Extension
class CoreBlocksCollector() : BlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        return listOf (
            //temperature
            ComparisonBlockFactory(Temperature::class.java, CommonBlockCategories.Temperature),
            EquationBlockFactory(Temperature::class.java, CommonBlockCategories.Temperature),
            TemperatureValueInCBlockFactory(),
            TemperatureValueInKBlockFactory(),
            TemperatureValueInFBlockFactory(),

            //humidity
            ComparisonBlockFactory(Humidity::class.java, CommonBlockCategories.Humidity),
            EquationBlockFactory(Humidity::class.java, CommonBlockCategories.Humidity),
            HumidityValueBlockFactory(),

            //luminosity
            ComparisonBlockFactory(Luminosity::class.java, CommonBlockCategories.Luminosity),
            EquationBlockFactory(Luminosity::class.java, CommonBlockCategories.Luminosity),
            LuminosityValueBlockFactory(),

            //wattage
            ComparisonBlockFactory(Wattage::class.java, CommonBlockCategories.Wattage),
            EquationBlockFactory(Wattage::class.java, CommonBlockCategories.Wattage),
            WattageValueBlockFactory(),

            //power level
            ComparisonBlockFactory(PowerLevel::class.java, CommonBlockCategories.PowerLevel),
            EquationBlockFactory(PowerLevel::class.java, CommonBlockCategories.PowerLevel),
            PowerLevelValueBlockFactory()
        )
    }
}
package eu.geekhome.automation.blocks

import eu.geekhome.automation.BlockFactory
import eu.geekhome.automation.R
import eu.geekhome.rest.getConfigurables
import eu.geekhome.rest.getRepository
import eu.geekhome.services.automation.StateType
import eu.geekhome.services.configurable.ConditionConfigurable
import eu.geekhome.services.configurable.Configurable
import eu.geekhome.services.configurable.StateDeviceConfigurable
import eu.geekhome.services.hardware.Humidity
import eu.geekhome.services.hardware.Temperature
import eu.geekhome.services.hardware.Wattage
import eu.geekhome.services.localization.Resource
import org.pf4j.PluginManager

interface IBlockFactoriesCollector {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}

class BlockFactoriesCollector(private val pluginManager: PluginManager) : IBlockFactoriesCollector {

    companion object {
        const val COLOR_TRIGGER = 315
        const val COLOR_LOGIC = 120
        const val COLOR_TEMPERATURE = 160
        const val COLOR_HUMIDITY = 210
        const val COLOR_WATTAGE = 65
    }

   override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        val result = ArrayList<BlockFactory<*>>()

        result.addAll(collectStaticBlocks())
        result.addAll(collectConditionBlocks())

        if (thisDevice != null) {
            result.addAll(collectChangeStateBlocks(thisDevice))
        }

        return result
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        return listOf(
            LogicAndBlockFactory(COLOR_LOGIC),
            LogicOrBlockFactory(COLOR_LOGIC),
            LogicNotBlockFactory(COLOR_LOGIC),
            LogicIfElseBlockFactory(COLOR_LOGIC),
            TimeloopTriggerBlockFactory(COLOR_TRIGGER),

            //temperature
            ComparisonBlockFactory(Temperature::class.java, R.category_temperature,COLOR_TEMPERATURE),
            EquationBlockFactory(Temperature::class.java, R.category_temperature, COLOR_TEMPERATURE),
            TemperatureValueInCBlockFactory(COLOR_TEMPERATURE),
            TemperatureValueInKBlockFactory(COLOR_TEMPERATURE),
            TemperatureValueInFBlockFactory(COLOR_TEMPERATURE),

            //humidity
            ComparisonBlockFactory(Humidity::class.java, R.category_humidity, COLOR_HUMIDITY),
            EquationBlockFactory(Humidity::class.java, R.category_humidity, COLOR_HUMIDITY),
            HumidityValueBlockFactory(COLOR_HUMIDITY),

            //wattage
            ComparisonBlockFactory(Wattage::class.java, R.category_wattage,COLOR_WATTAGE),
            EquationBlockFactory(Wattage::class.java, R.category_wattage, COLOR_WATTAGE),
            WattageValueBlockFactory(COLOR_WATTAGE)
        )
    }

    private fun collectConditionBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = pluginManager.getRepository().getAllInstanceBriefs()
        val allConfigurables = pluginManager.getConfigurables()

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is ConditionConfigurable
            }
            .map { briefDto ->
                val conditionId = briefDto.id
                val label = Resource.createUniResource(briefDto.name)
                ConditionBlockFactory(conditionId, label)
            }
            .toList()
    }

    private fun collectChangeStateBlocks(thisDevice: Configurable): List<BlockFactory<*>> {
        if (thisDevice is StateDeviceConfigurable) {
            return thisDevice
                .states
                .filter { it.value.type != StateType.ReadOnly }
                .map {
                    ChangeStateBlockFactory(it.value)
                }
                .toList()
        }

        return listOf()
    }
}
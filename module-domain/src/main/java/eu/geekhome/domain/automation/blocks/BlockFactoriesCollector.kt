package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.extensibility.PluginsCoordinator
import eu.geekhome.domain.automation.BlockFactory
import eu.geekhome.domain.R
import eu.geekhome.data.automation.StateType
import eu.geekhome.domain.configurable.*
import eu.geekhome.domain.hardware.Humidity
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.hardware.Temperature
import eu.geekhome.domain.hardware.Wattage
import eu.geekhome.data.localization.Resource
import eu.geekhome.data.Repository

interface IBlockFactoriesCollector {
    fun collect(thisDevice: Configurable?): List<BlockFactory<*>>
}

enum class CategoryConstants(
    val valueType: Class<out PortValue>,
    val categoryName: Resource,
    val color: Int
) {
    CategoryTriggers(Nothing::class.java, R.category_triggers, 315),
    CategoryLogic(Nothing::class.java, R.category_logic, 120),
    CategoryTemperature(Temperature::class.java, R.category_temperature, 160),
    CategoryWattage(Wattage::class.java, R.category_wattage, 65),
    CategoryHumidity(Humidity::class.java, R.category_humidity, 210);

    companion object {
        fun fromType(type: Class<out PortValue>): CategoryConstants {
            return values().first { it.valueType == type }
        }
    }
}

class BlockFactoriesCollector(private val pluginsCoordinator: PluginsCoordinator,
                              private val repository: Repository
) : IBlockFactoriesCollector {

    override fun collect(thisDevice: Configurable?): List<BlockFactory<*>> {
        val result = ArrayList<BlockFactory<*>>()

        result.addAll(collectStaticBlocks())
        result.addAll(collectConditionBlocks())
        result.addAll(collectSensorBlocks())

        if (thisDevice != null) {
            result.addAll(collectChangeStateBlocks(thisDevice))
        }

        return result
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        return listOf(
            //logic
            LogicAndBlockFactory(CategoryConstants.CategoryLogic.color),
            LogicOrBlockFactory(CategoryConstants.CategoryLogic.color),
            LogicNotBlockFactory(CategoryConstants.CategoryLogic.color),
            LogicIfElseBlockFactory(CategoryConstants.CategoryLogic.color),

            //triggers
            TimeloopTriggerBlockFactory(CategoryConstants.CategoryTriggers.color),

            //temperature
            ComparisonBlockFactory(Temperature::class.java,
                CategoryConstants.CategoryTemperature.categoryName,
                CategoryConstants.CategoryTemperature.color),
            EquationBlockFactory(Temperature::class.java,
                CategoryConstants.CategoryTemperature.categoryName,
                CategoryConstants.CategoryTemperature.color),
            TemperatureValueInCBlockFactory(CategoryConstants.CategoryTemperature.color),
            TemperatureValueInKBlockFactory(CategoryConstants.CategoryTemperature.color),
            TemperatureValueInFBlockFactory(CategoryConstants.CategoryTemperature.color),

            //humidity
            ComparisonBlockFactory(Humidity::class.java,
                CategoryConstants.CategoryHumidity.categoryName,
                CategoryConstants.CategoryHumidity.color),
            EquationBlockFactory(Humidity::class.java,
                CategoryConstants.CategoryHumidity.categoryName,
                CategoryConstants.CategoryHumidity.color),
            HumidityValueBlockFactory(CategoryConstants.CategoryHumidity.color),

            //wattage
            ComparisonBlockFactory(Wattage::class.java,
                CategoryConstants.CategoryWattage.categoryName,
                CategoryConstants.CategoryWattage.color),
            EquationBlockFactory(Wattage::class.java,
                CategoryConstants.CategoryWattage.categoryName, CategoryConstants.CategoryWattage.color),
            WattageValueBlockFactory(CategoryConstants.CategoryWattage.color)
        )
    }

    private fun collectConditionBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is ConditionConfigurable
            }
            .map { briefDto ->
                val id = briefDto.id
                val label = Resource.createUniResource(briefDto.name)
                ConditionBlockFactory(id, label)
            }
            .toList()
    }

    private fun collectSensorBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .map {  briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                Pair(briefDto, configurable)
            }
            .filter {
                it.second is SensorConfigurable<*>
            }
            .map { (briefDto, configurable) ->
                val id = briefDto.id
                val sensorConfigurable = configurable as SensorConfigurable<*>
                val label = Resource.createUniResource(briefDto.name)
                val category = CategoryConstants.fromType(sensorConfigurable.valueType)
                SensorBlockFactory(sensorConfigurable.valueType, category.categoryName, category.color, id, label)
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
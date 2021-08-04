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
    Triggers(Nothing::class.java, R.category_triggers, 315),
    Logic(Nothing::class.java, R.category_logic, 120),
    Temperature(eu.geekhome.domain.hardware.Temperature::class.java, R.category_temperature, 160),
    Wattage(eu.geekhome.domain.hardware.Wattage::class.java, R.category_wattage, 65),
    Humidity(eu.geekhome.domain.hardware.Humidity::class.java, R.category_humidity, 210),
    State(Nothing::class.java, R.category_state, 100);

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
        result.addAll(collectChangeStateTriggerBlocks())
        result.addAll(collectStateValuesBlocks())

        if (thisDevice != null) {
            result.addAll(collectChangeStateBlocks(thisDevice))
        }

        return result
    }

    private fun collectStaticBlocks(): List<BlockFactory<*>> {
        return listOf(
            //logic
            LogicAndBlockFactory(CategoryConstants.Logic.color),
            LogicOrBlockFactory(CategoryConstants.Logic.color),
            LogicNotBlockFactory(CategoryConstants.Logic.color),
            LogicIfElseBlockFactory(CategoryConstants.Logic.color),

            //triggers
            TimeloopTriggerBlockFactory(CategoryConstants.Triggers.color),

            //temperature
            ComparisonBlockFactory(Temperature::class.java,
                CategoryConstants.Temperature.categoryName,
                CategoryConstants.Temperature.color),
            EquationBlockFactory(Temperature::class.java,
                CategoryConstants.Temperature.categoryName,
                CategoryConstants.Temperature.color),
            TemperatureValueInCBlockFactory(CategoryConstants.Temperature.color),
            TemperatureValueInKBlockFactory(CategoryConstants.Temperature.color),
            TemperatureValueInFBlockFactory(CategoryConstants.Temperature.color),

            //humidity
            ComparisonBlockFactory(Humidity::class.java,
                CategoryConstants.Humidity.categoryName,
                CategoryConstants.Humidity.color),
            EquationBlockFactory(Humidity::class.java,
                CategoryConstants.Humidity.categoryName,
                CategoryConstants.Humidity.color),
            HumidityValueBlockFactory(CategoryConstants.Humidity.color),

            //wattage
            ComparisonBlockFactory(Wattage::class.java,
                CategoryConstants.Wattage.categoryName,
                CategoryConstants.Wattage.color),
            EquationBlockFactory(Wattage::class.java,
                CategoryConstants.Wattage.categoryName, CategoryConstants.Wattage.color),
            WattageValueBlockFactory(CategoryConstants.Wattage.color)
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

    private fun collectChangeStateTriggerBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is StateDeviceConfigurable
            }
            .map { briefDto ->
                val instanceId = briefDto.id
                val deviceName = briefDto.name ?: "---"
                val configurableClazz = allConfigurables.find { it.javaClass.name == briefDto.clazz } as StateDeviceConfigurable
                StateChangeTriggerBlockFactory(
                    CategoryConstants.Triggers.color,
                    instanceId,
                    deviceName,
                    configurableClazz.states)
            }
            .toList()
    }

    private fun collectStateValuesBlocks(): List<BlockFactory<*>> {
        val instanceBriefs = repository.getAllInstanceBriefs()
        val allConfigurables = pluginsCoordinator.configurables

        return instanceBriefs
            .filter { briefDto ->
                val configurable = allConfigurables.find { it.javaClass.name == briefDto.clazz }
                configurable is StateDeviceConfigurable
            }
            .map { briefDto ->
                val instanceId = briefDto.id
                val deviceName = briefDto.name ?: "---"
                val configurableClazz = allConfigurables.find { it.javaClass.name == briefDto.clazz } as StateDeviceConfigurable
                StateValueBlockFactory(
                    deviceName,
                    instanceId,
                    configurableClazz.states,
                )
            }
            .toList()
    }
}
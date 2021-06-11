package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.domain.configurable.SensorConfigurable
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.localization.Resource

class SensorBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: Resource,
    private val color: Int,
    private val sensorId: Long,
    private val label: Resource) : ValueBlockFactory {

    override val type: String = "temperature_sensor_$sensorId"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": $color,
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": "${valueType.simpleName}" }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type.startsWith("temperature_sensor_")
    }

    override fun transform(
        block: Block,
        next: IStatementNode?,
        context: AutomationContext,
        transformer: IBlocklyTransformer
    ): IValueNode {
        val evaluator = context.automationUnitsCache[this.sensorId]

        if (evaluator != null) {
            return SensorAutomationNode(evaluator)
        }

        throw MalformedBlockException(block.type,
            "it's impossible to connect this block with correct ${SensorConfigurable::class.java}")
    }
}
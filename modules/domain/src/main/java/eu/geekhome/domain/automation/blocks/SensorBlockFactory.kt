package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.configurable.SensorConfigurable
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.data.localization.Resource

class SensorBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    override val category: BlockCategory,
    private val sensorId: Long,
    private val label: Resource) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName}_sensor_$sensorId"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   { "type":  "$type",
                     "colour": ${category.color},
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": "${valueType.simpleName}" }
                """.trimIndent()
        }
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
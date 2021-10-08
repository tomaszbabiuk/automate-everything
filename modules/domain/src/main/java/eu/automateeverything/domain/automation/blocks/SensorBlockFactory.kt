package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.configurable.SensorConfigurable
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.data.localization.Resource

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
        next: StatementNode?,
        context: AutomationContext,
        transformer: BlocklyTransformer
    ): ValueNode {
        val evaluator = context.automationUnitsCache[this.sensorId]

        if (evaluator != null) {
            return SensorAutomationNode(evaluator)
        }

        throw MalformedBlockException(block.type,
            "it's impossible to connect this block with correct ${SensorConfigurable::class.java}")
    }
}
package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.hardware.PortValueBuilder

open class SimpleValueBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    private val minValue: Double,
    private val maxValue: Double?,
    private val initialValue: Double,
    private val unit: String,
    typeSuffix: String,
    private val valueConverter: ValueConverter?,
    override val category: BlockCategory) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName.lowercase()}_value$typeSuffix"

    private fun buildMaxValueAttribute(maxValue: Double?): String {
        return if (maxValue != null) {
            "\"max\": $maxValue,"
        } else {
            ""
        }
    }
    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "%1 %2",
                  "args0": [
                    {
                      "type": "field_number",
                      "name": "VALUE",
                      "value": $initialValue,
                      "min": $minValue,
                      ${buildMaxValueAttribute(maxValue)}
                      "precision": 0.01
                    },
                    {
                      "type": "field_label_serializable",
                      "name": "UNIT",
                      "text": "$unit"
                    }
                  ],
                  "inputsInline": true,
                  "output": "${valueType.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        if (block.fields == null || block.fields.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <FIELDS> defined: VALUE and UNIT")
        }

        val valueField = block.fields.find { it.name == "VALUE" }
        if (valueField == null) {
            throw MalformedBlockException(block.type, "should have <field name=\"VALUE\"> defined")
        } else if (valueField.value != null) {
            var value = valueField.value.toDouble()
            if (valueConverter != null) {
                value = valueConverter.convert(value)
            }
            return BasicValueNode(PortValueBuilder.buildFromDouble(valueType,value))
        }

        throw MalformedBlockException(block.type, "cannot extract temperature value")
    }
}
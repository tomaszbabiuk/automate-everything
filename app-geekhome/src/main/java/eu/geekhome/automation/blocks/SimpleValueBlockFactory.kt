package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.domain.hardware.PortValue
import eu.geekhome.domain.hardware.PortValueBuilder
import eu.geekhome.domain.localization.Resource



open class SimpleValueBlockFactory<T: PortValue>(
    private val valueType: Class<T>,
    private val minValue: Double,
    private val maxValue: Double,
    private val initialValue: Double,
    private val unit: String,
    typeSuffix: String,
    private val valueConverter: IValueConverter?,
    override val category: Resource,
    private val color: Int) : ValueBlockFactory {

    override val type: String = "${valueType.simpleName.toLowerCase()}_value$typeSuffix"

    override fun match(type: String) : Boolean {
        return type == this.type
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
                      "max": $maxValue,
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
                  "colour": $color,
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IValueNode {
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
            return ValueNode(PortValueBuilder.buildFromDouble(valueType,value))
        }

        throw MalformedBlockException(block.type, "cannot extract temperature value")
    }
}
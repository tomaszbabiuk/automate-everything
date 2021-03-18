package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.hardware.Temperature
import eu.geekhome.services.localization.Resource

class TemperatureValueInCBlockFactory(private val color: Int) : ValueBlockFactory {

    override val category: Resource = R.category_temperature

    override val type: String = "temperature_value_c"

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
                      "value": 0,
                      "min": -273.15,
                      "max": 10000,
                      "precision": 0.01
                    },
                    {
                      "type": "field_label_serializable",
                      "name": "UNIT",
                      "text": "°C"
                    }
                  ],
                  "inputsInline": true,
                  "output": "Temperature",
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
            val value = valueField.value.toDouble()
            val valueAsTemp = Temperature(value - 273.15)
            return ValueNode(valueAsTemp)
        }

        throw MalformedBlockException(block.type, "cannot extract temperature value")
    }
}
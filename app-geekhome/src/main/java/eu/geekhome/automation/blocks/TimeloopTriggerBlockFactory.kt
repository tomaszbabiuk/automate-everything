package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

class TimeloopTriggerBlockFactory : TriggerBlockFactory {

    override val category: Resource = R.category_name_triggers

    override val type: String = "trigger_timeloop"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                   {
                   "type": "$type",
                   "message0": "${R.block_label_repeat.getValue(it)}",
                   "args0": [
                     {
                       "type": "field_dropdown",
                       "name": "SECONDS",
                       "options": [
                         ["${R.second.getValue(it)}", "1"],
                         ["${R.minute.getValue(it)}", "60"],
                         ["${R.hour.getValue(it)}", "3600"]
                       ]
                     }
                   ],
                   "nextStatement": "Boolean",
                   "colour": 315,
                   "tooltip": null,
                   "helpUrl": null
                }
                """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type == this.type
    }

    override fun transform(
        block: Block,
        next: StatementNode?,
        context: AutomationContext,
        transformer: IBlocklyTransformer
    ): StatementNode {

        if (block.field == null) {
            throw MalformedBlockException(block.type, "should have <field> defined")
        }

        if (block.field.value == null) {
            throw MalformedBlockException(block.type, "should have <field/value> defined")
        }

        val seconds = block.field.value.toInt()

        return TimeTriggerBlock(seconds, next)
    }
}
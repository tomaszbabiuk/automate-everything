package eu.geekhome.domain.automation.blocks

import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.domain.localization.Resource

class TimeloopTriggerBlockFactory(private val color: Int) : TriggerBlockFactory {

    override val category: Resource = R.category_triggers

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
                   "colour": $color,
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
        next: IStatementNode?,
        context: AutomationContext,
        transformer: IBlocklyTransformer
    ): IStatementNode {

        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have <field> defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have only one field")
        }

        if (block.fields[0].value == null) {
            throw MalformedBlockException(block.type, "should have <field/value> with content")
        }

        val seconds = block.fields[0].value!!.toInt()

        return TimeTriggerBlock(seconds, next)
    }
}
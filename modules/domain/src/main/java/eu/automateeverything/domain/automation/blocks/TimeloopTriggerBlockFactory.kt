package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class TimeloopTriggerBlockFactory : TriggerBlockFactory {

    enum class LoopTime(val label: Resource, val seconds: Int) {
        Second(R.second, 1),
        Seconds15(R.seconds15, 15),
        Seconds30(R.seconds30, 30),
        Minute(R.minute, 60),
        Minutes2(R.minutes2, 120),
        Minutes5(R.minutes5, 300),
        Minutes10(R.minutes10, 600),
        Minutes30(R.minutes30, 1800),
        Hour(R.hour, 3600);

        companion object {
            fun fromString(raw: String): LoopTime {
                return values().first { it.name == raw }
            }
        }
    }

    override val category = CommonBlockCategories.Triggers

    override val type: String = "trigger_timeloop"

    override fun buildBlock(): RawJson {
        return RawJson { language ->
            """
                   {
                   "type": "$type",
                   "message0": "${R.block_label_repeat.getValue(language)}",
                   "args0": [
                     {
                       "type": "field_dropdown",
                       "name": "SECONDS",
                       "options": ${LoopTime.values().joinToString(prefix="[", postfix = "]") { "[\"${it.label.getValue(language)}\", \"${it}\"]" }}
                     }
                   ],
                   "nextStatement": "Boolean",
                   "colour": ${category.color},
                   "tooltip": null,
                   "helpUrl": null
                }
                """.trimIndent()
        }
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

        return TimeTriggerNode(seconds, next)
    }
}
package eu.automateeverything.timeplugin

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*

open class NowBlockFactory: ValueBlockFactory {

    override val category = TimeBlockCategories.Time

    override val type: String = "timeofday_value"

    override fun buildBlock(): RawJson {

        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_now_label.getValue(it)}",
                  "output": "${Timestamp::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        return NowValueNode()
    }
}


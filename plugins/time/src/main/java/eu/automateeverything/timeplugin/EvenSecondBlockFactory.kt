package eu.automateeverything.timeplugin

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories

open class EvenSecondBlockFactory: EvaluatorBlockFactory {

    override val category = TimeBlockCategories.SecondOfDay

    override val type: String = "evensecond_value"

    override fun buildBlock(): RawJson {

        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_evensecond_label.getValue(it)}",
                  "output": "${Boolean::class.java.simpleName}",
                  "colour": ${CommonBlockCategories.Logic.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {
        return EvenSecondValueNode()
    }
}


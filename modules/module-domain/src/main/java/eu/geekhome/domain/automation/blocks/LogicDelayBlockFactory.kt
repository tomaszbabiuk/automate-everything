package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.data.localization.Resource

class LogicDelayBlockFactory : StatementBlockFactory {

    override val category = CategoryConstants.Logic

    override val type: String = "logic_delay"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_delay.getValue(it)}",
                  "args0": [
                    {
                      "type": "field_number",
                      "name": "DELAY",
                      "value": 0,
                      "min": 0
                    },
                    {
                      "type": "input_dummy"
                    },
                    {
                      "type": "input_statement",
                      "name": "DO"
                    }
                  ],
                  "inputsInline": true,
                  "previousStatement": null,
                  "nextStatement": null,
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
            """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IStatementNode {
        var doNode: IStatementNode? = null

        if (block.statements != null) {
            val doStatement = block.statements.find { it.name == "DO" }
            if (doStatement == null) {
                throw MalformedBlockException(block.type, "should have <DO statement> defined")
            } else if (doStatement.block != null) {
                doNode = transformer.transformStatement(doStatement.block, context)
            }
        }

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

        return DelayAutomationNode(next, doNode, seconds)
    }
}
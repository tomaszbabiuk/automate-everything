package eu.geekhome.automation.blocks

import eu.geekhome.automation.*
import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

class LogicIfElseBlockFactory : StatementBlockFactory {

    override val category: Resource = R.category_name_logic

    override val type: String = "logic_if_than_else"

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_label_if_than_else.getValue(it)}",
                  "args0": [
                    {
                      "type": "input_value",
                      "name": "CONDITION",
                      "check": "Boolean",
                      "align": "RIGHT"
                    },
                    {
                      "type": "input_statement",
                      "name": "IF",
                      "align": "RIGHT"
                    },
                    {
                      "type": "input_statement",
                      "name": "ELSE",
                      "align": "RIGHT"
                    }
                  ],
                  "previousStatement": null,
                  "nextStatement": null,
                  "colour": 120,
                  "tooltip": null,
                  "helpUrl": null
                }
            """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type == this.type
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IStatementNode {
        var ifNode: IStatementNode? = null
        var elseNode: IStatementNode? = null

        if (block.statements != null) {
            val ifStatement = block.statements.find { it.name == "IF" }
            if (ifStatement == null) {
                throw MalformedBlockException(block.type, "should have <IF statement> defined")
            } else if (ifStatement.block != null) {
                ifNode = transformer.transformStatement(ifStatement.block, context)
            }

            val elseStatement = block.statements.find { it.name == "ELSE" }
            if (elseStatement == null) {
                throw MalformedBlockException(block.type, "should have <ELSE statement> defined")
            } else if (elseStatement.block != null) {
                elseNode = transformer.transformStatement(elseStatement.block, context)
            }
        }

        if (block.values == null) {
            throw MalformedBlockException(block.type, "should have at least <VALUE> defined")
        }

        if (block.values.size != 1) {
            throw MalformedBlockException(block.type, "should have only one <VALUE>")
        }

        val valueNode = transformer.transformEvaluator(block.values[0].block!!, context)

        return IfThanElseAutomationNode(next, valueNode, ifNode, elseNode)
    }
}
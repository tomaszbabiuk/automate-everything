package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.automation.State
import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.*
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.R


open class StateValueBlockFactory(
    private val deviceName: String,
    instanceId: Long,
    private val states: Map<String, State>,
) : ValueBlockFactory {

    override val type: String = "state_value_$instanceId"

    override val category: Resource = CategoryConstants.State.categoryName

    override fun match(type: String) : Boolean {
        return type == this.type
    }

    override fun buildBlock(): RawJson {
        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "\"$deviceName\" ${R.block_label_in_state.getValue(it)}",
                  "args0": [
                    {
                      "type": "input_dummy",
                      "align": "CENTRE"
                    },
                    {
                       "type": "field_dropdown",
                       "name": "STATE",
                       "options": [
                         ${buildStateOptions(states, it)}
                       ]
                     }
                  ],
                  "inputsInline": true,
                  "output": "Boolean",
                  "colour": ${CategoryConstants.State.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IValueNode {

        throw MalformedBlockException(block.type, "cannot extract temperature value")
    }
}
package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.R


open class StateValueBlockFactory(
    private val deviceName: String,
    instanceId: Long,
    private val states: Map<String, State>,
) : EvaluatorBlockFactory {

    private val typePrefix = "state_value_"

    override val type: String = typePrefix + instanceId

    override val category = CommonBlockCategories.State

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
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {

        if (block.fields == null) {
            throw MalformedBlockException(block.type, "should have <field> defined")
        }

        if (block.fields.size != 1) {
            throw MalformedBlockException(block.type, "should have only one field")
        }

        if (block.fields[0].value == null) {
            throw MalformedBlockException(block.type, "should have <field/value> with content")
        }

        val stateId = block.fields[0].value!!

        val instanceIdRaw = block.type.replace(typePrefix, "")
        val instanceId = instanceIdRaw.toLong()
        val triggerUnit = context.automationUnitsCache[instanceId] as StateDeviceAutomationUnitBase

        return IsInStateAutomationNode(triggerUnit, stateId)
    }
}
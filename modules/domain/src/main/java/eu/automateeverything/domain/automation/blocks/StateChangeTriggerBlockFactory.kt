package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.R
import eu.automateeverything.domain.automation.*

class StateChangeTriggerBlockFactory(
    instanceId: Long,
    private val deviceName: String,
    private val states: Map<String, State>) : TriggerBlockFactory {

    override val category = CommonBlockCategories.Triggers

    private val typePrefix = "trigger_statedevice_"
    override val type: String = typePrefix + instanceId

    override fun buildBlock(): RawJson {

        return RawJson {
            """
               {
               "type": "$type",
               "message0": "\"$deviceName\" ${R.block_label_changes_state.getValue(it)}",
               "args0": [
                {
                  "type": "input_dummy",
                  "align": "CENTRE"
                },
                 {
                   "type": "field_dropdown",
                   "name": "STATE_ID",
                   "options": [
                     ${buildStateOptions(states, it)}
                   ]
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

        val stateId = block.fields[0].value!!

        val instanceIdRaw = block.type.replace(typePrefix, "")
        val instanceId = instanceIdRaw.toLong()
        val triggerUnit = context.automationUnitsCache[instanceId] as StateDeviceAutomationUnit

        return StateDeviceTriggerNode(context, instanceId, triggerUnit, stateId, next)
    }
}
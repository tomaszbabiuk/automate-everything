package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.data.localization.Resource

class StateChangeTriggerBlockFactory(
    private val color: Int,
    instanceId: Long,
    private val deviceName: String,
    states: Map<String, State>) : TriggerBlockFactory {

    override val category: Resource = R.category_triggers

    private val statesToControl = states.filter { it.value.type == StateType.Control }

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
                     ${buildStateOptions(statesToControl, it)}
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
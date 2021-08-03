package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.blocks.RawJson
import eu.geekhome.data.localization.Language
import eu.geekhome.domain.R
import eu.geekhome.domain.automation.*
import eu.geekhome.data.localization.Resource
import java.lang.StringBuilder

class StateDeviceTriggerBlockFactory(
    private val color: Int,
    instanceId: Long,
    private val deviceName: String,
    private val states: Map<String, State>) : TriggerBlockFactory {

    override val category: Resource = R.category_triggers

    private val typePrefix = "trigger_statedevice_"
    override val type: String = typePrefix + instanceId

    private fun buildStateOption(state: State, language: Language): String {
        return """["${state.name.getValue(language)}", "${state.id}"]"""
    }

    private fun buildStateOptions(states: Map<String, State>, language: Language): String {
        val result = StringBuilder()
        states
            .filter { it.value.type == StateType.Control }
            .forEach{
            if (result.isNotEmpty()) {
                result.append(",")
            }
            result.append(buildStateOption(it.value, language))
        }

        return result.toString()
    }

    override fun buildBlock(): RawJson {

        return RawJson {
            """
               {
               "type": "$type",
               "message0": "\"$deviceName\" ${R.block_label_state.getValue(it)}",
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
               "colour": $color,
               "tooltip": null,
               "helpUrl": null
            }
            """.trimIndent()
        }
    }

    override fun match(type: String): Boolean {
        return type.startsWith(this.type)
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
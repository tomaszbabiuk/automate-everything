/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.bashactionplugin

import eu.automateeverything.actions.ActionAutomationUnit
import eu.automateeverything.actions.ActionConfigurableBase
import eu.automateeverything.data.automation.State
import eu.automateeverything.data.instances.InstanceDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.AutomationUnit
import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.domain.configurable.RequiredStringValidator
import eu.automateeverything.domain.configurable.StringField
import eu.automateeverything.domain.events.EventBus
import org.pf4j.Extension
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import java.util.stream.Collectors


@Extension
class BashScriptActionConfigurable(
    private val eventBus: EventBus
) : ActionConfigurableBase() {

    override val titleRes: Resource
        get() = R.configurable_bash_script_action_title

    override val descriptionRes: Resource
        get() = R.configurable_bash_script_action_description

    override val iconRaw: String
        get() = """
            <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com" data-name="Layer 1">
             <title>big4_outline</title>
             <g class="layer">
              <title>terminal by Hare Krishna from the Noun Project</title>
              <path d="m87.83,12.83l-75.66,0a8,8 0 0 0 -8,8l0,54.67a8,8 0 0 0 8,8l75.66,0a8,8 0 0 0 8,-8l0,-54.67a8,8 0 0 0 -8,-8zm4,62.67a4,4 0 0 1 -4,4l-75.66,0a4,4 0 0 1 -4,-4l0,-54.67a4,4 0 0 1 4,-4l75.66,0a4,4 0 0 1 4,4l0,54.67zm-60,-40.33a2,2 0 0 1 0,2.83l-8.83,8.83a2,2 0 0 1 -2.83,-2.83l7.42,-7.42l-7.5,-7.5a2,2 0 0 1 2.83,-2.83l8.91,8.92zm21.17,10.25a2,2 0 0 1 -2,2l-14.5,0a2,2 0 0 1 0,-4l14.5,0a2,2 0 0 1 2,2z" id="svg_1"/>
             </g>
            </svg>
        """.trimIndent()

    private val commandField = StringField(FIELD_COMMAND, R.field_command_hint, 0, "sudo shutdown -h 0", RequiredStringValidator())

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() {
            val result: MutableMap<String, FieldDefinition<*>> = HashMap(super.fieldDefinitions)
            result[FIELD_COMMAND] = commandField
            return result
        }

    override fun buildAutomationUnit(instance: InstanceDto): AutomationUnit<State> {
        val name = instance.fields[FIELD_NAME]!!
        return ActionAutomationUnit(eventBus, instance, name, states) {
            executeScript(instance)
        }
    }

    private fun executeScript(instance: InstanceDto): Pair<Boolean,Resource> {
        val cmd = extractFieldValue(instance, commandField)
        val run = Runtime.getRuntime()
        try {
            val pr = run.exec(cmd)
            pr.waitFor()

            val buf = BufferedReader(InputStreamReader(pr.inputStream))
            val result = Resource.createUniResource(buf.lines().collect(Collectors.joining(System.lineSeparator())))
            val success = pr.exitValue() == 0
            return Pair(success, result)
        } catch (ex: Exception) {
            val result = Resource.createUniResource(ex.localizedMessage)
            return Pair(false, result)
        }
    }

    override val addNewRes = R.configurable_bash_script_action_add

    override val editRes = R.configurable_bash_script_action_edit

    companion object {
        const val FIELD_COMMAND = "command"
    }
}
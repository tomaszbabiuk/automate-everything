package eu.geekhome.domain.automation.blocks

import eu.geekhome.data.automation.State
import eu.geekhome.data.automation.StateType
import eu.geekhome.data.localization.Language
import java.lang.StringBuilder

fun buildStateOption(state: State, language: Language): String {
    return """["${state.name.getValue(language)}", "${state.id}"]"""
}

fun buildStateOptions(states: Map<String, State>, language: Language): String {
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

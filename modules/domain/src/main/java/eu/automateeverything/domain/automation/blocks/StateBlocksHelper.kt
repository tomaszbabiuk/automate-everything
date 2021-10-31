package eu.automateeverything.domain.automation.blocks

import eu.automateeverything.data.automation.State
import eu.automateeverything.data.localization.Language
import java.lang.StringBuilder

fun buildStateOption(state: State, language: Language): String {
    return """["${state.name.getValue(language)}", "${state.id}"]"""
}

fun buildStateOptions(states: Map<String, State>, language: Language): String {
    val result = StringBuilder()
    states
        .forEach{
            if (result.isNotEmpty()) {
                result.append(",")
            }
            result.append(buildStateOption(it.value, language))
        }

    return result.toString()
}
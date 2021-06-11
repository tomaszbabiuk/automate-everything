package eu.geekhome.domain.automation

import eu.geekhome.domain.localization.Resource

data class State(
    val id: String,
    val name: Resource,
    val type: StateType,
    val isSignaled: Boolean,
    var codeRequired: Boolean)
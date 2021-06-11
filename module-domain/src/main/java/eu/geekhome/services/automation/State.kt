package eu.geekhome.services.automation

import eu.geekhome.services.localization.Resource

data class State(
    val id: String,
    val name: Resource,
    val type: StateType,
    val isSignaled: Boolean,
    var codeRequired: Boolean)
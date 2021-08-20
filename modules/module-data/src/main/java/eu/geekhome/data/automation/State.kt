package eu.geekhome.data.automation

import eu.geekhome.data.localization.Resource

data class  State(
    val id: String,
    val name: Resource,
    val action: Resource,
    val type: StateType,
    val isSignaled: Boolean,
    var codeRequired: Boolean)
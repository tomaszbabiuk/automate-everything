package eu.automateeverything.data.automation

import eu.automateeverything.data.localization.Resource

abstract class  State(
    val id: String,
    val name: Resource,
    val action: Resource?,
    val type: StateType,
    val isSignaled: Boolean = false,
    var codeRequired: Boolean = false)

class ReadOnlyState(
    id: String,
    name: Resource,
    isSignaled: Boolean = false,
    codeRequired: Boolean = false
) : State(id, name, null, StateType.ReadOnly, isSignaled, codeRequired)

class ControlState(
    id: String,
    name: Resource,
    action: Resource,
    isSignaled: Boolean = false,
    codeRequired: Boolean = false
) : State(id, name, action, StateType.Control, isSignaled, codeRequired)

package eu.geekhome.data.automation

data class NextStatesDto(
    val states: List<State>,
    val current: String,
    val extendedWidth: Boolean
)
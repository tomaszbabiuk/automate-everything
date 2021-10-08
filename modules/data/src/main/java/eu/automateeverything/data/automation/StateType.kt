package eu.automateeverything.data.automation

enum class StateType(private val index: Int) {
    ReadOnly(0), Control(1);

    override fun toString(): String {
        return index.toString()
    }
}
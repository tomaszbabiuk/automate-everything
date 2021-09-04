package eu.automateeverything.crypto

data class CurrencyPair(
    val base: String,
    val counter: String
) {
    override fun toString(): String {
        return "${base.uppercase()}/${counter.uppercase()}"
    }
}

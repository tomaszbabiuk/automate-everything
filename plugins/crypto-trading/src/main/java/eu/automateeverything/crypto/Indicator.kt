package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource

enum class Indicator(val label: Resource) {
    RSI14(R.indicator_rsi14), Ema21(R.indicator_ema21);

    companion object {
        fun fromString(raw: String): Indicator {
            return values().first { it.name == raw }
        }
    }
}
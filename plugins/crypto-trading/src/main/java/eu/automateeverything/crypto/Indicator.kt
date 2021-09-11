package eu.automateeverything.crypto

import eu.geekhome.data.localization.Resource

enum class Indicator(val label: Resource) {
    RSI14(R.indicator_rsi14),
    Ema21(R.indicator_ema21),
    Ema34(R.indicator_ema34),
    Ema55(R.indicator_ema55),
    Ema89(R.indicator_ema89),
    Ema200(R.indicator_ema200),
    Sma50(R.indicator_sma50),
    Sma100(R.indicator_sma100),
    Sma200(R.indicator_sma200),
    Roc100(R.indicator_roc100),
    BollingerBandsUpper(R.indicator_bol_upper),
    BollingerBandsMiddle(R.indicator_bol_middle),
    BollingerBandsLower(R.indicator_bol_lower),
    BollingerWidth(R.indicator_bol_width);

    companion object {
        fun fromString(raw: String): Indicator {
            return values().first { it.name == raw }
        }
    }
}
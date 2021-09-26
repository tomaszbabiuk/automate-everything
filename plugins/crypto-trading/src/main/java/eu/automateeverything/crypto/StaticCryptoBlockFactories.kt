package eu.automateeverything.crypto

import eu.automateeverything.domain.automation.blocks.ComparisonBlockFactory
import eu.automateeverything.domain.automation.blocks.EquationBlockFactory
import eu.automateeverything.domain.automation.blocks.SimpleValueBlockFactory

class TickerComparisonBlockFactory :
    ComparisonBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

class TickerEquationBlockFactory :
    EquationBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

class TickerValueBlockFactory : SimpleValueBlockFactory<Ticker>(
    Ticker::class.java,
    0.0,
    null,
    0.0,
    "-/-",
    "",
    null,
    CryptoBlockCategories.Crypto
)
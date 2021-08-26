package eu.automateeverything.crypto

import eu.geekhome.domain.automation.blocks.ComparisonBlockFactory
import eu.geekhome.domain.automation.blocks.EquationBlockFactory
import eu.geekhome.domain.automation.blocks.SimpleValueBlockFactory
import org.pf4j.Extension

@Extension
class TickerComparisonBlockFactory :
    ComparisonBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

@Extension
class TickerEquationBlockFactory :
    EquationBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

@Extension
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
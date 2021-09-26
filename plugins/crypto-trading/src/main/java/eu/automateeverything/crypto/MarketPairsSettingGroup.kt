package eu.automateeverything.crypto

import eu.automateeverything.domain.configurable.FieldDefinition
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.configurable.StringField

class MarketPairsSettingGroup : SettingGroup {

    override val titleRes = R.market_pairs_title
    override val descriptionRes = R.market_pairs_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_MARKET_PAIRS, StringField(FIELD_MARKET_PAIRS, R.field_currencies, 0, FIELD_MARKET_PAIRS_IV))
    )

    companion object {
        const val FIELD_MARKET_PAIRS = "market_pairs"
        const val FIELD_MARKET_PAIRS_IV = "BTC/USD, ETH/USD"
    }
}
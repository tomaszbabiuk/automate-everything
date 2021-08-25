package eu.automateeverything.crypto

import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.configurable.StringField

class MarketPairsSettingGroup : SettingGroup {

    override val titleRes = R.market_pairs_title
    override val descriptionRes = R.market_pairs_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair(FIELD_MARKET_PAIRS, StringField(FIELD_MARKET_PAIRS, R.field_currencies, 0, "BTC,ETH,XRP"))
    )

    companion object {
        const val FIELD_MARKET_PAIRS = "market_pairs"
    }
}
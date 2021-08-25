package eu.automateeverything.crypto.binance

import eu.automateeverything.crypto.R
import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.configurable.StringField

class BinanceApiSettingGroup : SettingGroup {

    override val titleRes = R.binance_settings_title
    override val descriptionRes = R.binance_settings_description

    override val fieldDefinitions: Map<String, FieldDefinition<*>> = mapOf(
        Pair("xxx", StringField("xxx", R.plugin_name, 0, ""))
    )
}
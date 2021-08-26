package eu.automateeverything.crypto

import eu.automateeverything.crypto.binance.BinanceApiSettingGroup
import eu.geekhome.data.localization.Resource
import eu.geekhome.domain.configurable.SettingGroup
import eu.geekhome.domain.extensibility.PluginMetadata
import eu.geekhome.domain.hardware.HardwareAdapterFactory
import eu.geekhome.domain.hardware.HardwarePlugin
import org.pf4j.PluginWrapper


class CryptoTradingPlugin(wrapper: PluginWrapper): HardwarePlugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override fun getFactory(): HardwareAdapterFactory {
        return CryptoTradingHardwareAdapterFactory()
    }


    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val settingGroups: List<SettingGroup> = listOf(
        MarketPairsSettingGroup(),
        BinanceApiSettingGroup()
    )

    companion object {
        const val PLUGIN_ID = "cryptotrading"
    }
}
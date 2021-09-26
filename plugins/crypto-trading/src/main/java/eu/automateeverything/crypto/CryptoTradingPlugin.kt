package eu.automateeverything.crypto

import eu.automateeverything.crypto.coingeckoapi.CoinGeckoApi
import eu.automateeverything.crypto.coingeckoapi.CoinGeckoMarketProxy
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.configurable.SettingGroup
import eu.automateeverything.domain.extensibility.PluginMetadata
import eu.automateeverything.domain.hardware.HardwareAdapter
import eu.automateeverything.domain.hardware.HardwarePlugin
import org.pf4j.PluginWrapper

class CryptoTradingPlugin(wrapper: PluginWrapper): HardwarePlugin(wrapper), PluginMetadata {

    override fun start() {
    }

    override fun stop() {
    }

    override fun createAdapters(): List<HardwareAdapter<*>> {
        val coinGeckoProxy = CoinGeckoMarketProxy(CoinGeckoApi())
        val coinGeckoAdapter = ExchangeHardwareAdapter("Coingecko", pluginId, coinGeckoProxy)

        return listOf(coinGeckoAdapter)
    }

    override val name: Resource = R.plugin_name
    override val description: Resource = R.plugin_description
    override val settingGroups: List<SettingGroup> = listOf(
        MarketPairsSettingGroup()
    )
}
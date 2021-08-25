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
//        val bitfinex: Exchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange::class.java)
//        val marketDataService = bitfinex.marketDataService
//        val ticker: Ticker = marketDataService.getTicker(CurrencyPair.BTC_USD)
//        val now = Calendar.getInstance().timeInMillis
//        val nowMinus21Days = now - (3600 * 1000 * 24 * 21)
//        val candles = (marketDataService as BitfinexMarketDataService).getHistoricCandles("1D", CurrencyPair.BTC_USD, 100, nowMinus21Days, now,0)
//        println(candles)
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
        const val PLUGIN_ID_CRYPTO_TRADING = "cryptotrading"
    }
}
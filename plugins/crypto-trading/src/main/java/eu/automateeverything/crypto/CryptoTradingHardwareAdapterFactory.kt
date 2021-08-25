package eu.automateeverything.crypto

import eu.automateeverything.crypto.binance.BitfinexCryptoHardwareAdapter
import eu.geekhome.domain.hardware.HardwareAdapter
import eu.geekhome.domain.hardware.HardwareAdapterFactory

class CryptoTradingHardwareAdapterFactory : HardwareAdapterFactory {
    override fun createAdapters(): List<HardwareAdapter<*>> {
        return listOf(BitfinexCryptoHardwareAdapter())
    }

    override val owningPluginId = CryptoTradingPlugin.PLUGIN_ID_CRYPTO_TRADING
}

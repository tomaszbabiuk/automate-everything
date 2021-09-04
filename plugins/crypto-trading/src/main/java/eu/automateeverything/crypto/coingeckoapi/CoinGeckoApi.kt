package eu.automateeverything.crypto.coingeckoapi

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

data class Coin(
    val id: String,
    val symbol: String,
    val name: String
)

data class MarketResponse(
    val prices: Map<Long, Double>,
    val total_volumes: Map<Long, Double>
)

class CoinGeckoApi {
    private val client = createHttpClient()

    suspend fun listBaseCoins() : List<Coin> {
        return client.request("https://api.coingecko.com/api/v3/coins/list")
    }

    suspend fun listTickers(geckoIdsOfBase: List<String>, geckoIdsOfCounter: List<String>) : Map<String,Map<String,Double>> {
        return client.request("https://api.coingecko.com/api/v3/simple/price") {
            parameter("ids", geckoIdsOfBase.joinToString(","))
            parameter("vs_currencies", geckoIdsOfCounter.joinToString(","))
        }
    }

    suspend fun marketChart(baseGeckoId: String, counterGeckoId: String, fromUnixTimestamp: Long, toUnixTimestamp: Long) : MarketResponse {
        return client.request("https://api.coingecko.com/api/v3/coins/${baseGeckoId}/market_chart/range") {
            parameter("from", fromUnixTimestamp)
            parameter("to", toUnixTimestamp)
            parameter("vs_currency", counterGeckoId)
        }
    }

    private fun createHttpClient() = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }

        engine {
            maxConnectionsCount = 100

            endpoint {
                maxConnectionsPerRoute = 10
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 10000
                connectAttempts = 5
            }

        }
    }
}
/*
 * Copyright (c) 2019-2022 Tomasz Babiuk
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.automateeverything.crypto.coingeckoapi

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*

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
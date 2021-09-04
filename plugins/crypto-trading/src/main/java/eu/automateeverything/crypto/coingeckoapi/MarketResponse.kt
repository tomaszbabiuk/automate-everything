package eu.automateeverything.crypto.coingeckoapi

data class MarketResponse(
    val prices: Map<Long, Double>,
    val total_volumes: Map<Long, Double>
)
/*
 * Copyright (c) 2019-2021 Tomasz Babiuk
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

@file:Suppress("FunctionName")

package eu.automateeverything.crypto

import eu.automateeverything.data.localization.Resource

object R {
    val indicator_bol_upper = Resource(
        "Upper Bollinger band",
        "Górna wstęga Bollingera"
    )

    val indicator_bol_middle = Resource(
        "Middle Bollinger band",
        "Środkowa wstęga Bollingera"
    )

    val indicator_bol_lower = Resource(
        "Lower Bollinger band",
        "Dolna wstęga Bollingera"
    )

    val indicator_bol_width = Resource(
        "Bollinger width",
        "Szerokość wstęgi Bollingera"
    )

    val indicator_ema21 = Resource(
        "EMA-21",
        "EMA-21"
    )

    val indicator_ema34 = Resource(
        "EMA-34",
        "EMA-34"
    )

    val indicator_ema55 = Resource(
        "EMA-55",
        "EMA-55"
    )

    val indicator_ema89 = Resource(
        "EMA-89",
        "EMA-89"
    )

    val indicator_ema200 = Resource(
        "EMA-21",
        "EMA-21"
    )

    val indicator_sma50 = Resource(
        "SMA-50",
        "SMA-50"
    )

    val indicator_sma100 = Resource(
        "SMA-100",
        "SMA-100"
    )

    val indicator_sma200 = Resource(
        "SMA-200",
        "SMA-200"
    )

    val indicator_rsi14 = Resource(
        "RSI-14",
        "RSI-14"
    )

    val indicator_roc100 = Resource(
        "ROC-100",
        "ROC-100"
    )

    val interval_day = Resource(
            "day",
            "dzień"
    )

    val interval_week = Resource(
            "week",
            "tydzień"
    )

    val interval_hour = Resource(
            "hour",
            "godzina"
    )

    val category_crypto = Resource(
        "Cryptocurrencies",
        "Kryptowaluty"
    )

    val field_port_hint = Resource(
        "Port",
        "Port"
    )

    val configurable_ticker_description = Resource(
        "Crypto tickers data (from different markets)",
        "Kursy kryptowalut (z różnych rynków)"
    )

    val configurable_ticker_title = Resource(
        "Crypto tickers",
        "Kursy kryptowalut"
    )

    val configurable_ticker_edit = Resource(
        "Edit ticker",
        "Edytuj tiker"
    )

    val configurable_ticker_add = Resource(
        "Add ticker",
        "Dodaj tiker"
    )

    val field_currencies = Resource(
        "Currencies",
        "Waluty"
    )
    val market_pairs_description = Resource(
        "Enter the market pairs you are interested in (comma separated), like: BTC/USD, BTC/USDT, etc...",
        "Wprowadź pary kryptowalut, którymi jesteś zainteresowany (oddzielone przecinkiem), np: BTC/USD, BTC/USDT..."
    )

    val market_pairs_title = Resource(
        "Market pairs",
        "Pary walutowe"
    )

    val plugin_description = Resource(
        "Crypto trading automation blocks and objects. Unofficial support for Binance and Bitfinex trading platforms.",
        "Bloki i obiekty do handlowania kryptowalutami. Wsparcie dla giełd Binance i Bitfinex (nieoficjalne)."
    )

    val plugin_name = Resource("Crypto", "Krypto")

    fun block_indicator_label(pair: String) = Resource(
        "%1 of $pair  %2",
        "%1 od $pair  %2"
    )
}
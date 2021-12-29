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

package eu.automateeverything.crypto

import eu.automateeverything.data.localization.Resource

enum class Indicator(val label: Resource) {
    RSI14(R.indicator_rsi14),
    Ema21(R.indicator_ema21),
    Ema34(R.indicator_ema34),
    Ema55(R.indicator_ema55),
    Ema89(R.indicator_ema89),
    Ema200(R.indicator_ema200),
    Sma50(R.indicator_sma50),
    Sma100(R.indicator_sma100),
    Sma200(R.indicator_sma200),
    Roc100(R.indicator_roc100),
    BollingerBandsUpper(R.indicator_bol_upper),
    BollingerBandsMiddle(R.indicator_bol_middle),
    BollingerBandsLower(R.indicator_bol_lower),
    BollingerWidth(R.indicator_bol_width);

    companion object {
        fun fromString(raw: String): Indicator {
            return values().first { it.name == raw }
        }
    }
}
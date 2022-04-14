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

package eu.automateeverything.crypto

import org.ta4j.core.BaseBar
import java.math.BigDecimal

interface MarketProxy {
    suspend fun getTickers(currencyFilter: List<CurrencyPair>): Map<CurrencyPair, BigDecimal>
    suspend fun getWeeklyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getDailyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
    suspend fun getHourlyData(pair: CurrencyPair, fromTimestamp: Long, toTimestamp: Long): List<BaseBar>
}
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

import eu.automateeverything.domain.automation.blocks.ComparisonBlockFactory
import eu.automateeverything.domain.automation.blocks.EquationBlockFactory
import eu.automateeverything.domain.automation.blocks.SimpleValueBlockFactory

class TickerComparisonBlockFactory :
    ComparisonBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

class TickerEquationBlockFactory :
    EquationBlockFactory<Ticker>(Ticker::class.java, CryptoBlockCategories.Crypto)

class TickerValueBlockFactory : SimpleValueBlockFactory<Ticker>(
    Ticker::class.java,
    0.0,
    null,
    0.0,
    "-/-",
    "",
    null,
    CryptoBlockCategories.Crypto
)
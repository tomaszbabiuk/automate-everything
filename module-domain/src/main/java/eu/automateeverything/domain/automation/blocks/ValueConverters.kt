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

package eu.automateeverything.domain.automation.blocks

import java.math.BigDecimal

interface ValueConverter {
    fun convert(x: BigDecimal) : BigDecimal
}

class CelsiusToKelvinValueConverter : ValueConverter {
    override fun convert(x: BigDecimal): BigDecimal {
        return x + 273.15.toBigDecimal()
    }
}

class FahrenheitToKelvinValueConverter : ValueConverter {
    override fun convert(x: BigDecimal): BigDecimal {
        return  (x + 459.67.toBigDecimal()) * 5.0.toBigDecimal()/9.0.toBigDecimal()
    }
}
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

package eu.automateeverything.domain.hardware

import eu.automateeverything.data.hardware.PortValue
import java.io.InvalidClassException
import java.math.BigDecimal


class PortValueBuilder {
    companion object {
        fun <T : PortValue> buildFromDecimal(valueClazz: Class<in T>, x: BigDecimal): PortValue {

            valueClazz.constructors.forEach {
                if (it.parameters.size == 1 && it.parameters[0].type == BigDecimal::class.java) {
                    return it.newInstance(x) as PortValue
                }
            }

            throw InvalidClassException("ValueClazz $valueClazz should have at least one constructor with double parameter. Otherwise it cannot be constructed manually")
        }
    }
}
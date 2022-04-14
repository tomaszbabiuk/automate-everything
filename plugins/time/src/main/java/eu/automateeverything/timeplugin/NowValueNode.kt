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

package eu.automateeverything.timeplugin

import eu.automateeverything.domain.automation.ValueNode
import eu.automateeverything.data.hardware.PortValue
import java.util.*

class NowValueNode : ValueNode {
    override fun getValue(now: Calendar): PortValue {
        val nowSeconds = now.get(Calendar.HOUR_OF_DAY)*3600 + now.get(Calendar.MINUTE)*60 + now.get(Calendar.SECOND)
        return SecondOfDayStamp(nowSeconds.toBigDecimal())
    }
}
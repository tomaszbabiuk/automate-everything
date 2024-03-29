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

package eu.automateeverything.mappers

import eu.automateeverything.data.hardware.PortDto
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.hardware.Port
import java.math.BigDecimal

class PortDtoMapper {
    fun map(port: Port<*>, factoryId: String, adapterId: String): PortDto {
        val decimalValue: BigDecimal? =
            if (port.capabilities.canRead) {
                port.read().asDecimal()
            } else null
        val interfaceValue: Resource? =
            if (port.capabilities.canRead) {
                port.read().toFormattedString()
            } else null
        return PortDto(
            port.portId,
            factoryId,
            adapterId,
            decimalValue,
            interfaceValue,
            port.valueClazz.name,
            port.capabilities.canRead,
            port.capabilities.canWrite,
            port.maxSleepInterval,
            port.lastSeenTimestamp
        )
    }
}

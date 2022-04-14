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

package eu.automateeverything.aforeplugin

import eu.automateeverything.domain.hardware.InputPort
import eu.automateeverything.domain.hardware.Port
import eu.automateeverything.domain.hardware.Wattage
import io.ktor.client.*
import io.ktor.client.request.*
import java.math.BigDecimal
import java.net.InetAddress
import java.util.*

class AforeWattageInputPort(
    override val id: String,
    private val httpClient: HttpClient,
    private val inetAddress: InetAddress
) : Port<Wattage>, InputPort<Wattage> {

    override val valueClazz = Wattage::class.java

    override val sleepInterval: Long
        get() = 1000 * 60 * 5L

    override var lastSeenTimestamp = 0L

    suspend fun refresh(now: Calendar) {
        try {
            refreshInverterData()
            lastSeenTimestamp = now.timeInMillis
        } catch (ignored: Exception) {
        }
    }

    private var cachedValue = Wattage(BigDecimal.ZERO)

    override fun read(): Wattage {
        return cachedValue
    }

    private suspend fun refreshInverterData() {
        val newValue = readInverterPower()
        if (cachedValue.value != newValue) {
            cachedValue = Wattage(newValue)
        }
    }

    private suspend fun readInverterPower(): BigDecimal {
        val inverterResponse = httpClient.get<String>("http://$inetAddress/status.html")
        val lines = inverterResponse.split(";").toTypedArray()
        for (line in lines) {
            if (line.contains("webdata_now_p")) {
                val s = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""))
                return s.toBigDecimal()
            }
        }

        return BigDecimal.ZERO
    }
}
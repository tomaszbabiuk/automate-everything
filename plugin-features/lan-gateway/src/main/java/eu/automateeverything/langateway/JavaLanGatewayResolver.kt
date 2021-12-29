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

package eu.automateeverything.langateway

import eu.automateeverything.domain.langateway.LanGateway
import eu.automateeverything.domain.langateway.LanGatewayResolver
import java.net.Inet4Address
import java.net.NetworkInterface

class JavaLanGatewayResolver : LanGatewayResolver {
    override fun resolve(): List<LanGateway> {
        return NetworkInterface
            .getNetworkInterfaces()
            .toList()
            .filterNot { it.displayName.contains("VMware") }
            .filterNot { it.displayName.contains("VirtualBox") }
            .filter { networkInterface ->
                networkInterface.inetAddresses.toList()
                    .filterIsInstance<Inet4Address>()
                    .any { address -> address.isSiteLocalAddress }
            }
            .map {
                val interfaceName = it.displayName
                val address = it.inetAddresses.toList().filterIsInstance<Inet4Address>().first()
                LanGateway(interfaceName, address)
            }
    }
}
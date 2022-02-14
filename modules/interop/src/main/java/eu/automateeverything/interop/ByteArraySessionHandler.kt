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

package eu.automateeverything.interop

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

class ByteArraySessionHandler(
    private val internalHandler: SessionHandler<JsonRpc2Request, JsonRpc2Response>,
    private val binaryFormat: BinaryFormat)
: SessionHandler<ByteArray, ByteArray> {

    override fun handleRequest(input: ByteArray): ByteArray {
        val requests = binaryFormat.decodeFromByteArray<List<JsonRpc2Request>>(input)
        val processed = requests
            .map(internalHandler::handleRequest)

        return binaryFormat.encodeToByteArray(processed)
    }
}
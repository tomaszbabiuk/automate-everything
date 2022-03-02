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

package eu.automateeverything.interop.handlers

import eu.automateeverything.data.Repository
import eu.automateeverything.data.tags.TagDto
import eu.automateeverything.interop.JsonRpc2SessionHandler
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.encodeToByteArray

class TagsHandler(val repository: Repository) : JsonRpc2SessionHandler.MethodHandler {
        override fun matches(method: String): Boolean {
            return method == TagDto::class.java.simpleName
        }

        override fun handle(format: BinaryFormat, params: ByteArray?): ByteArray {
            val result = repository.getAllTags()
            return format.encodeToByteArray(result)
        }
    }
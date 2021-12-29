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

package eu.automateeverything.domain.firstrun

import java.io.File

class FileCheckingFirstRunService() : FirstRunService {
    companion object {
        const val MARK_FILE_NAME = "firstrun.marker"
    }

    private val markingFile = File(MARK_FILE_NAME)

    override fun isFirstRun(): Boolean {
        return !markingFile.exists()
    }

    override fun markFirstRunHappened() {
        markingFile.createNewFile()
    }
}
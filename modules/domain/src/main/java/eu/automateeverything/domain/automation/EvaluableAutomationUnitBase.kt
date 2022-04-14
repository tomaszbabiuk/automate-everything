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

package eu.automateeverything.domain.automation

import java.lang.Exception
import kotlin.Throws
import java.util.Calendar

abstract class EvaluableAutomationUnitBase : EvaluableAutomationUnit {
    private var lastEvaluationTime: Long = 0

    override var isPassed = false

    @Throws(Exception::class)
    override fun evaluate(now: Calendar): Boolean {
        if (lastEvaluationTime != now.timeInMillis) {
            isPassed = doEvaluate(now)
            lastEvaluationTime = now.timeInMillis
        }
        return isPassed
    }

    @Throws(Exception::class)
    protected abstract fun doEvaluate(now: Calendar): Boolean
}
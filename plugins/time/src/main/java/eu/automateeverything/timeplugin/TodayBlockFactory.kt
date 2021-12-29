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

package eu.automateeverything.timeplugin

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*

open class TodayBlockFactory: ValueBlockFactory {

    override val category = TimeBlockCategories.DayOfYear

    override val type: String = "today_value"

    override fun buildBlock(): RawJson {

        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_today_label.getValue(it)}",
                  "output": "${DayOfYearStamp::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        return TodayValueNode()
    }
}


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
import eu.automateeverything.domain.automation.blocks.CommonBlockCategories

open class EvenSecondBlockFactory: EvaluatorBlockFactory {

    override val category = TimeBlockCategories.SecondOfDay

    override val type: String = "evensecond_value"

    override fun buildBlock(): RawJson {

        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "${R.block_evensecond_label.getValue(it)}",
                  "output": "${Boolean::class.java.simpleName}",
                  "colour": ${CommonBlockCategories.Logic.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): EvaluatorNode {
        return EvenSecondValueNode()
    }
}


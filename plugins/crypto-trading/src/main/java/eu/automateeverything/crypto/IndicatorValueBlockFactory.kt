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

package eu.automateeverything.crypto

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*

open class IndicatorValueBlockFactory(
    private val currencyPair: CurrencyPair,
    private val tickerPort: MarketPort,
) : ValueBlockFactory {
    override val category = CryptoBlockCategories.Crypto

    override val type: String = "crypto_indicator_${currencyPair.base.lowercase()}_${currencyPair.counter.lowercase()}"

    override fun buildBlock(): RawJson {
        return RawJson { language ->
            """
                {
                  "type": "$type",
                  "message0": "${R.block_indicator_label(currencyPair.toString()).getValue(language)}",
                  "args0": [
                    {
                      "type": "field_dropdown",
                      "name": "INDICATOR",
                      "options": ${Indicator.values().joinToString(prefix="[", postfix = "]") { "[\"${it.label.getValue(language)}\", \"${it}\"]" }}
                    },
                    {
                      "type": "field_dropdown",
                      "name": "INTERVAL",
                      "options": ${Interval.values().joinToString(prefix="[", postfix = "]") { "[\"${it.label.getValue(language)}\", \"${it}\"]" }}
                    }
                  ],
                  "inputsInline": false,
                  "output": "${Ticker::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
            """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        if (block.fields == null || block.fields!!.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <FIELDS> defined: INDICATOR and INTERVAL")
        }

        val indicatorField = block.fields!!.find { it.name == "INDICATOR" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"INDICATOR\"> defined")

        val intervalField = block.fields!!.find { it.name == "INTERVAL" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"INTERVAL\"> defined")

        val interval = Interval.fromString(intervalField.value!!)
        val indicator = Indicator.fromString(indicatorField.value!!)

        return IndicatorValueNode(tickerPort, indicator, interval)
    }
}
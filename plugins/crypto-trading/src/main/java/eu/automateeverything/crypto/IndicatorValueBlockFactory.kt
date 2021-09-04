package eu.automateeverything.crypto

import eu.geekhome.data.blocks.RawJson
import eu.geekhome.domain.automation.*

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

    override fun transform(block: Block, next: IStatementNode?, context: AutomationContext, transformer: IBlocklyTransformer): IValueNode {
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
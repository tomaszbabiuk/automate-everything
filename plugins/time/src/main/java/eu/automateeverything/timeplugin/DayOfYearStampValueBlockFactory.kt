package eu.automateeverything.timeplugin

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.data.localization.Resource
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.hardware.PortValueBuilder
import java.util.*

open class DayOfYearStampValueBlockFactory: ValueBlockFactory {

    enum class MonthOption(val label: Resource, val monthNo: Int) {
        January(R.month_january,0),
        February(R.month_february,1),
        March(R.month_march,2),
        April(R.month_april,3),
        May(R.month_may,4),
        June(R.month_june,5),
        July(R.month_july,6),
        August(R.month_august,7),
        September(R.month_september,8),
        October(R.month_october,9),
        November(R.month_november,10),
        December(R.month_december,11),
    }

    enum class DayOption(val label: String, val dayNo: Int) {
        D01("01",1),
        D02("02",2),
        D03("03",3),
        D04("04",4),
        D05("05",5),
        D06("06",6),
        D07("07",7),
        D08("08",8),
        D09("09",9),
        D10("10",10),
        D11("11",11),
        D12("12",12),
        D13("13",13),
        D14("14",14),
        D15("15",15),
        D16("16",16),
        D17("17",17),
        D18("18",18),
        D19("19",19),
        D20("20",20),
        D21("21",21),
        D22("22",22),
        D23("23",23),
        D24("24",24),
        D25("25",25),
        D26("26",26),
        D27("27",27),
        D28("28",28),
        D29("(29)",29),
        D30("(30)",30),
        D31("(31)",31),
    }

    override val category = TimeBlockCategories.DayOfYear

    override val type: String = "${DayOfYearStamp::class.java.simpleName.lowercase()}_value"

    override fun buildBlock(): RawJson {

        return RawJson {    language ->
            """
                {
                  "type": "$type",
                  "message0": "${R.block_dayofyear_message.getValue(language)}",
                  "args0": [
                    {
                      "type": "field_dropdown",
                      "name": "MONTH",
                      "options": ${ MonthOption.values().joinToString(prefix = "[", postfix = "]") { "[\"${it.label.getValue(language)}\", \"${it.monthNo}\"]" }}
                    },
                    {
                      "type": "field_dropdown",
                      "name": "DAY",
                      "options": ${ DayOption.values().joinToString(prefix = "[", postfix = "]") { "[\"${it.label}\", \"${it.dayNo}\"]" }}                      
                    }
                  ],
                  "inputsInline": true,
                  "output": "${DayOfYearStamp::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        if (block.fields == null || block.fields!!.size != 2) {
            throw MalformedBlockException(block.type, "should have exactly two <FIELDS> defined: MONTH and DAY")
        }

        val monthField = block.fields!!.find { it.name == "MONTH" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"MONTH\"> defined")

        val dayField = block.fields!!.find { it.name == "DAY" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"DAY\"> defined")

        val cal = Calendar.getInstance()
        cal.set(0, monthField.value!!.toInt(), dayField.value!!.toInt())
        val totalDays = cal.get(Calendar.DAY_OF_YEAR)
        val totalDaysValue = PortValueBuilder.buildFromDouble(DayOfYearStamp::class.java, totalDays.toDouble())

        return BasicValueNode(totalDaysValue)
    }
}
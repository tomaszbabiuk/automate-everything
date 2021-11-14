package eu.automateeverything.timeplugin

import eu.automateeverything.data.blocks.RawJson
import eu.automateeverything.domain.automation.*
import eu.automateeverything.domain.automation.blocks.BlockCategory
import eu.automateeverything.domain.hardware.PortValue
import eu.automateeverything.domain.hardware.PortValueBuilder
import eu.automateeverything.timeplugin.Timestamp
import java.sql.Time

enum class HourEnumOption(val label: String, val seconds: Int) {
    H00("00",3600 * 0),
    H01("01",3600 * 1),
    H02("02",3600 * 2),
    H03("03",3600 * 3),
    H04("04",3600 * 4),
    H05("05",3600 * 5),
    H06("06",3600 * 6),
    H07("07",3600 * 7),
    H08("08",3600 * 8),
    H09("09",3600 * 9),
    H10("10",3600 * 10),
    H11("11",3600 * 11),
    H12("12",3600 * 12),
    H13("13",3600 * 13),
    H14("14",3600 * 14),
    H15("15",3600 * 15),
    H16("16",3600 * 16),
    H17("17",3600 * 17),
    H18("18",3600 * 18),
    H19("19",3600 * 19),
    H20("20",3600 * 20),
    H21("21",3600 * 21),
    H22("22",3600 * 22),
    H23("23",3600 * 23),
}

enum class MinuteEnumOption(val label: String, val seconds: Int) {
    M00("00",60 * 0),
    M01("01",60 * 1),
    M02("02",60 * 2),
    M03("03",60 * 3),
    M04("04",60 * 4),
    M05("05",60 * 5),
    M06("06",60 * 6),
    M07("07",60 * 7),
    M08("08",60 * 8),
    M09("09",60 * 9),
    M10("10",60 * 10),
    M11("11",60 * 11),
    M12("12",60 * 12),
    M13("13",60 * 13),
    M14("14",60 * 14),
    M15("15",60 * 15),
    M16("16",60 * 16),
    M17("17",60 * 17),
    M18("18",60 * 18),
    M19("19",60 * 19),
    M20("20",60 * 20),
    M21("21",60 * 21),
    M22("22",60 * 22),
    M23("23",60 * 23),
    M24("24",60 * 24),
    M25("25",60 * 25),
    M26("26",60 * 26),
    M27("27",60 * 27),
    M28("28",60 * 28),
    M29("29",60 * 29),
    M30("30",60 * 30),
    M31("31",60 * 31),
    M32("32",60 * 32),
    M33("33",60 * 33),
    M34("34",60 * 34),
    M35("35",60 * 35),
    M36("36",60 * 36),
    M37("37",60 * 37),
    M38("38",60 * 38),
    M39("39",60 * 39),
    M40("40",60 * 40),
    M41("41",60 * 41),
    M42("42",60 * 42),
    M43("43",60 * 43),
    M44("44",60 * 44),
    M45("45",60 * 45),
    M46("46",60 * 46),
    M47("47",60 * 47),
    M48("48",60 * 48),
    M49("49",60 * 49),
    M50("50",60 * 50),
    M51("51",60 * 51),
    M52("52",60 * 52),
    M53("53",60 * 53),
    M54("54",60 * 54),
    M55("55",60 * 55),
    M56("56",60 * 56),
    M57("57",60 * 57),
    M58("58",60 * 58),
    M59("59",60 * 59),
}

enum class SecondEnumOption(val label: String, val seconds: Int) {
    S00("00",0),
    S01("01",1),
    S02("02",2),
    S03("03",3),
    S04("04",4),
    S05("05",5),
    S06("06",6),
    S07("07",7),
    S08("08",8),
    S09("09",9),
    S10("10",10),
    S11("11",11),
    S12("12",12),
    S13("13",13),
    S14("14",14),
    S15("15",15),
    S16("16",16),
    S17("17",17),
    S18("18",18),
    S19("19",19),
    S20("20",20),
    S21("21",21),
    S22("22",22),
    S23("23",23),
    S24("24",24),
    S25("25",25),
    S26("26",26),
    S27("27",27),
    S28("28",28),
    S29("29",29),
    S30("30",30),
    S31("31",31),
    S32("32",32),
    S33("33",33),
    S34("34",34),
    S35("35",35),
    S36("36",36),
    S37("37",37),
    S38("38",38),
    S39("39",39),
    S40("40",40),
    S41("41",41),
    S42("42",42),
    S43("43",43),
    S44("44",44),
    S45("45",45),
    S46("46",46),
    S47("47",47),
    S48("48",48),
    S49("49",49),
    S50("50",50),
    S51("51",51),
    S52("52",52),
    S53("53",53),
    S54("54",54),
    S55("55",55),
    S56("56",56),
    S57("57",57),
    S58("58",58),
    S59("59",59),
}

open class TimestampValueBlockFactory(
) : ValueBlockFactory {

    override val category = TimeBlockCategories.Time

    override val type: String = "${Timestamp::class.java.simpleName.lowercase()}_value"

    override fun buildBlock(): RawJson {

        return RawJson {
            """
                {
                  "type": "$type",
                  "message0": "H: %1 M: %2 S: %3",
                  "args0": [
                    {
                      "type": "field_dropdown",
                      "name": "HOUR",
                      "options": ${ HourEnumOption.values().joinToString(prefix = "[", postfix = "]") { "[\"${it.label}\", \"${it.seconds}\"]" }}
                    },
                    {
                      "type": "field_dropdown",
                      "name": "MINUTE",
                      "options": ${ MinuteEnumOption.values().joinToString(prefix = "[", postfix = "]") { "[\"${it.label}\", \"${it.seconds}\"]" }}                      
                    },
                    {
                      "type": "field_dropdown",
                      "name": "SECOND",
                      "options": ${ SecondEnumOption.values().joinToString(prefix = "[", postfix = "]") { "[\"${it.label}\", \"${it.seconds}\"]" }}                                            
                    }
                  ],
                  "inputsInline": true,
                  "output": "${Timestamp::class.java.simpleName}",
                  "colour": ${category.color},
                  "tooltip": "",
                  "helpUrl": ""
                }
                """.trimIndent()
        }
    }

    override fun transform(block: Block, next: StatementNode?, context: AutomationContext, transformer: BlocklyTransformer): ValueNode {
        if (block.fields == null || block.fields!!.size != 3) {
            throw MalformedBlockException(block.type, "should have exactly two <FIELDS> defined: VALUE and UNIT")
        }

        val hourField = block.fields!!.find { it.name == "HOUR" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"HOUR\"> defined")

        val minuteField = block.fields!!.find { it.name == "MINUTE" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"MINUTE\"> defined")

        val secondField = block.fields!!.find { it.name == "SECOND" }
            ?: throw MalformedBlockException(block.type, "should have <field name=\"SECOND\"> defined")

        val totalSecondsRaw =
            hourField.value!!.toDouble() + minuteField.value!!.toDouble() + secondField.value!!.toDouble()
        val totalSecondsValue =
            PortValueBuilder.buildFromDouble(Timestamp::class.java, totalSecondsRaw) as Timestamp

        return BasicValueNode(totalSecondsValue)
    }
}
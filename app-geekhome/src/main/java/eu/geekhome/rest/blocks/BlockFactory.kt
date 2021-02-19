package eu.geekhome.rest.blocks

import eu.geekhome.rest.RawJson
import eu.geekhome.services.localization.Resource

class BlockFactory {
    companion object {
        fun buildChangeStateBlock(type:String, label:Resource) : RawJson {
            return RawJson {
                """
                   { "type":  "$type",
                     "colour": 230,
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "previousStatement": null,
                     "nextStatement": null }
                """.trimIndent()
            }
        }

        fun buildConditionStateBlock(type: String, label: Resource) : RawJson {
            return RawJson {
                """
                   { "type":  "$type",
                     "colour": 120,
                     "tooltip": null,
                     "helpUrl": null,
                     "message0": "${label.getValue(it)}",
                     "output": null }
                """.trimIndent()
            }
        }

        fun buildRepeatStateBlock(type: String): RawJson {
            return RawJson {
                """
                   {
                   "type": "$type",
                   "message0": "${R.block_label_repeat.getValue(it)}",
                   "args0": [
                     {
                       "type": "field_dropdown",
                       "name": "SECONDS",
                       "options": [
                         ["${R.second.getValue(it)}", "1"],
                         ["${R.minute.getValue(it)}", "60"],
                         ["${R.hour.getValue(it)}", "3600"]
                       ]
                     }
                   ],
                   "nextStatement": "Boolean",
                   "colour": 315,
                   "tooltip": null,
                   "helpUrl": null
                }
                """.trimIndent()
            }
        }

        fun buildIfThanElseBlock(type: String): RawJson {
            return RawJson {
                """
                    {
                      "type": "$type",
                      "message0": "${R.block_label_if_than_else.getValue(it)}",
                      "args0": [
                        {
                          "type": "input_value",
                          "name": "CONDITION",
                          "check": "Boolean",
                          "align": "RIGHT"
                        },
                        {
                          "type": "input_statement",
                          "name": "IF",
                          "align": "RIGHT"
                        },
                        {
                          "type": "input_statement",
                          "name": "ELSE",
                          "align": "RIGHT"
                        }
                      ],
                      "previousStatement": null,
                      "nextStatement": null,
                      "colour": 120,
                      "tooltip": null,
                      "helpUrl": null
                    }
                """.trimIndent()
            }
        }
    }
}

/*
var repeatBlock = {
    type: "ae_repeat",
    message0: "Powtarzaj co %1",
    args0: [
    {
        type: "field_dropdown",
        name: "NAME",
        options: [
        ["sekundę", "1"],
        ["minutę", "60"],
        ["godzinę", "3600"],
        ],
    },
    ],
    nextStatement: "Boolean",
    colour: 315,
    tooltip: "",
    helpUrl: "",
};*/
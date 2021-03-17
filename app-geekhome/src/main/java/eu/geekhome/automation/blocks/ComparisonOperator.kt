package eu.geekhome.automation.blocks

enum class ComparisonOperator(val str: String) {
    Greater("GREATER"),
    Lesser("LESSER"),
    Equals("EQUALS"),
    NotEquals("NOTEQUALS"),
    GreaterOrEqual("GREATEROREQUAL"),
    LesserOrEqual("LESSEROREQUAL");

    companion object {
        fun fromString(str:String): ComparisonOperator {
            return values().find { it.str == str }!!
        }
    }
}
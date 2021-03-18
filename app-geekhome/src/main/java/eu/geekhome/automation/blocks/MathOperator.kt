package eu.geekhome.automation.blocks

enum class MathOperator(val str: String) {
    Plus("PLUS"),
    Minus("MINUS"),
    Times("TIMES"),
    Divide("DIVIDE");

    companion object {
        fun fromString(str:String): MathOperator {
            return values().find { it.str == str }!!
        }
    }
}
package eu.geekhome.shellyplugin

/*
{"ison":false,"mode":"white","brightness":22}
 */
data class LightResponseDto(

    val ison: Boolean,
    val mode: String,
    val brightness: Int
)
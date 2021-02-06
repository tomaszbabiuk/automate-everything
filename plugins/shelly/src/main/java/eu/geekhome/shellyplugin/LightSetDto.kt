package eu.geekhome.shellyplugin

/*
{"turn":"on", "brightness":22}
 */
data class LightSetDto(

    val turn: String,
    var brightness: Int
)
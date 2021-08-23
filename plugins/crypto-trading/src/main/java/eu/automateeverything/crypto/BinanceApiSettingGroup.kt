package eu.automateeverything.crypto

import eu.geekhome.domain.configurable.FieldDefinition
import eu.geekhome.domain.configurable.SettingGroup

class BinanceApiSettingGroup : SettingGroup {

    override val titleRes = R.binance_settings_title
    override val descriptionRes = R.binance_settings_description

    override val iconRaw = """
        <svg width="100" height="100" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" xmlns:se="http://svg-edit.googlecode.com" data-name="Layer 1">
         <g class="layer">
          <title>Bitcoin by iconpixel from The Noun Project</title>
          <g id="svg_3">
           <path d="m50,3.5a46.5,46.5 0 1 0 46.5,46.5a46.5527,46.5527 0 0 0 -46.5,-46.5zm0,86.8a40.3,40.3 0 1 1 40.3,-40.3a40.3465,40.3465 0 0 1 -40.3,40.3z" fill="black" id="svg_1"/>
           <path d="m68.6,40.7a12.4,12.4 0 0 0 -9.3,-11.9598l0,-3.5402a3.1,3.1 0 0 0 -6.2,0l0,3.1l-6.2,0l0,-3.1a3.1,3.1 0 0 0 -6.2,0l0,3.1l-9.3,0a3.1,3.1 0 0 0 0,6.2l3.1,0l0,31l-3.1,0a3.1,3.1 0 0 0 0,6.2l9.3,0l0,3.1a3.1,3.1 0 0 0 6.2,0l0,-3.1l6.2,0l0,3.1a3.1,3.1 0 0 0 6.2,0l0,-3.5402a12.3008,12.3008 0 0 0 5.0158,-21.2598a12.3225,12.3225 0 0 0 4.2842,-9.3zm-6.2,18.6a6.2,6.2 0 0 1 -6.2,6.2l-15.5,0l0,-12.4l15.5,0a6.2,6.2 0 0 1 6.2,6.2zm-21.7,-12.4l0,-12.4l15.5,0a6.2,6.2 0 0 1 0,12.4l-15.5,0z" fill="black" id="svg_2"/>
          </g>
         </g>
        </svg>
    """.trimIndent()

    override val fieldDefinitions: Map<String, FieldDefinition<*>>
        get() = TODO("Not yet implemented")
}
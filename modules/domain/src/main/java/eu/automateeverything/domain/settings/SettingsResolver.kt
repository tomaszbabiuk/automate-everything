package eu.automateeverything.domain.settings

import eu.automateeverything.data.settings.SettingsDto

interface SettingsResolver {
    fun resolve() : List<SettingsDto>
}


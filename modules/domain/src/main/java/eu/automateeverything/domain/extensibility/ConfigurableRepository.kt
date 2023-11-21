package eu.automateeverything.domain.extensibility

import eu.automateeverything.domain.configurable.Configurable

interface ConfigurableRepository {
    fun getAllConfigurables(): List<Configurable>
}

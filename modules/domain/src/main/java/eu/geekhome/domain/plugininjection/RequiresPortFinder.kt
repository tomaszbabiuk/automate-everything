package eu.geekhome.domain.plugininjection

import eu.geekhome.domain.hardware.PortFinder

interface RequiresPortFinder : AllFeaturesInjectedListener {
    fun injectPortFinder(portFinder: PortFinder)
}
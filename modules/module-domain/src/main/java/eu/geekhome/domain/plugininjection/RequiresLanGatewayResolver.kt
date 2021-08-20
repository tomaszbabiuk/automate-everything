package eu.geekhome.domain.plugininjection

import eu.geekhome.domain.langateway.LanGatewayResolver

interface RequiresLanGatewayResolver : AllFeaturesInjectedListener {
    fun injectLanGatewayResolver(resolver: LanGatewayResolver)
}
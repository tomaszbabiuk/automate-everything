package eu.automateeverything.domain.hardware

import kotlinx.coroutines.Deferred

data class AdapterBundle(
    val owningPluginId: String,
    val adapter: HardwareAdapter<*>,
) {
    var discoveryJob: Deferred<Unit>? = null
}
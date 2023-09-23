package eu.automateeverything.domain.coap

import kotlinx.serialization.Serializable

@Serializable
data class AutomateEverythingVersionManifest(val versionMajor: Int, val versionMinor: Int, val uniqueId: String)

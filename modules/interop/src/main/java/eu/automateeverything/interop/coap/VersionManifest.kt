package eu.automateeverything.interop.coap

import kotlinx.serialization.Serializable

@Serializable
data class VersionManifest(val versionMajor: Int, val versionMinor: Int, val uniqueId: String)

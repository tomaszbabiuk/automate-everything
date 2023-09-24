package eu.automateeverything.data.coap

import kotlinx.serialization.Serializable

@Serializable
data class VersionManifestDto(val versionMajor: Int, val versionMinor: Int, val uniqueId: String)

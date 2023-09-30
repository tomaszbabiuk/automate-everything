package eu.automateeverything.data.coap

import kotlinx.serialization.Serializable

@Serializable
data class ActiveSceneDto(val sceneId: String, val optionId: String? = null)

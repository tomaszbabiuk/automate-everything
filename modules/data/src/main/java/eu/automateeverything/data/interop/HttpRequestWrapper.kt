package eu.automateeverything.data.interop

import kotlinx.serialization.Serializable

@Serializable
enum class HttpMethod {
    GET, POST, PUT, PATCH, DELETE
}

@Serializable
data class HttpRequestWrapper(
    val url: String,
    val method: HttpMethod,
    val body: String? = null
)

@Serializable
data class HttpResponseWrapper(
    val code: Int,
    val body: String
)
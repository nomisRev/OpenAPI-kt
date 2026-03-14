package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class StatusCheckPolicy(
    val url: String,
    val strict: Boolean,
    val contexts: List<String>,
    val checks: List<Checks>,
    @SerialName("contexts_url") val contextsUrl: String,
) {
    @Serializable
    data class Checks(val context: String, @SerialName("app_id") val appId: Long?)
}

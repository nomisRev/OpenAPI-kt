package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProtectedBranchRequiredStatusCheck(
    val url: String? = null,
    @SerialName("enforcement_level") val enforcementLevel: String? = null,
    val contexts: List<String>,
    val checks: List<Checks>,
    @SerialName("contexts_url") val contextsUrl: String? = null,
    val strict: Boolean? = null,
) {
    @Serializable
    data class Checks(val context: String, @SerialName("app_id") val appId: Long?)
}

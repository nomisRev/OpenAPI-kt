package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LicenseSimple(
    val key: String,
    val name: String,
    val url: String?,
    @SerialName("spdx_id") val spdxId: String?,
    @SerialName("node_id") val nodeId: String,
    @SerialName("html_url") val htmlUrl: String? = null,
)

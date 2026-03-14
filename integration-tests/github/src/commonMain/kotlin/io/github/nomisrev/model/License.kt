package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class License(
    val key: String,
    val name: String,
    @SerialName("spdx_id") val spdxId: String?,
    val url: String?,
    @SerialName("node_id") val nodeId: String,
    @SerialName("html_url") val htmlUrl: String,
    val description: String,
    val implementation: String,
    val permissions: List<String>,
    val conditions: List<String>,
    val limitations: List<String>,
    val body: String,
    val featured: Boolean,
)

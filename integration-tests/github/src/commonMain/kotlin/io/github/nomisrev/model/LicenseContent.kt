package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LicenseContent(
    val name: String,
    val path: String,
    val sha: String,
    val size: Long,
    val url: String,
    @SerialName("html_url") val htmlUrl: String?,
    @SerialName("git_url") val gitUrl: String?,
    @SerialName("download_url") val downloadUrl: String?,
    val type: String,
    val content: String,
    val encoding: String,
    @SerialName("_links") val links: Links,
    val license: NullableLicenseSimple?,
) {
    @Serializable
    data class Links(val git: String?, val html: String?, val self: String)
}

package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ContentTree(
    val type: String,
    val size: Long,
    val name: String,
    val path: String,
    val sha: String,
    val content: String? = null,
    val url: String,
    @SerialName("git_url") val gitUrl: String?,
    @SerialName("html_url") val htmlUrl: String?,
    @SerialName("download_url") val downloadUrl: String?,
    val entries: List<Entries>? = null,
    val encoding: String? = null,
    @SerialName("_links") val links: Links,
) {
    @Serializable
    data class Entries(
        val type: String,
        val size: Long,
        val name: String,
        val path: String,
        val sha: String,
        val url: String,
        @SerialName("git_url") val gitUrl: String?,
        @SerialName("html_url") val htmlUrl: String?,
        @SerialName("download_url") val downloadUrl: String?,
        @SerialName("_links") val links: Links,
    ) {
        @Serializable
        data class Links(val git: String?, val html: String?, val self: String)
    }

    @Serializable
    data class Links(val git: String?, val html: String?, val self: String)
}

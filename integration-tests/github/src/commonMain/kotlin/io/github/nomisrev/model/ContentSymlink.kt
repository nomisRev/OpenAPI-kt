package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ContentSymlink(
    val type: Type,
    val target: String,
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
    enum class Type {
        @SerialName("symlink") Symlink;
    }

    @Serializable
    data class Links(val git: String?, val html: String?, val self: String)
}

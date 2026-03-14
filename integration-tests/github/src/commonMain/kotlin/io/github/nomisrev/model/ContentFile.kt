package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ContentFile(
    val type: Type,
    val encoding: String,
    val size: Long,
    val name: String,
    val path: String,
    val content: String,
    val sha: String,
    val url: String,
    @SerialName("git_url") val gitUrl: String?,
    @SerialName("html_url") val htmlUrl: String?,
    @SerialName("download_url") val downloadUrl: String?,
    @SerialName("_links") val links: Links,
    val target: String? = null,
    @SerialName("submodule_git_url") val submoduleGitUrl: String? = null,
) {
    @Serializable
    enum class Type {
        @SerialName("file") File;
    }

    @Serializable
    data class Links(val git: String?, val html: String?, val self: String)
}

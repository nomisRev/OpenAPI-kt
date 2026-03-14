package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class ContentDirectory(val items: List<Item>) {
    @Serializable
    data class Item(
        val type: Type,
        val size: Long,
        val name: String,
        val path: String,
        val content: String? = null,
        val sha: String,
        val url: String,
        @SerialName("git_url") val gitUrl: String?,
        @SerialName("html_url") val htmlUrl: String?,
        @SerialName("download_url") val downloadUrl: String?,
        @SerialName("_links") val links: Links,
    ) {
        @Serializable
        enum class Type {
            @SerialName("dir")
            Dir,
            @SerialName("file")
            File,
            @SerialName("submodule")
            Submodule,
            @SerialName("symlink")
            Symlink;
        }

        @Serializable
        data class Links(val git: String?, val html: String?, val self: String)
    }
}

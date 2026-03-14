package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeSearchResultItem(
    val name: String,
    val path: String,
    val sha: String,
    val url: String,
    @SerialName("git_url") val gitUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    val repository: MinimalRepository,
    val score: Double,
    @SerialName("file_size") val fileSize: Long? = null,
    val language: String? = null,
    @SerialName("last_modified_at") val lastModifiedAt: LocalDateTime? = null,
    @SerialName("line_numbers") val lineNumbers: List<String>? = null,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
)

package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DiffEntry(
    val sha: String?,
    val filename: String,
    val status: Status,
    val additions: Long,
    val deletions: Long,
    val changes: Long,
    @SerialName("blob_url") val blobUrl: String,
    @SerialName("raw_url") val rawUrl: String,
    @SerialName("contents_url") val contentsUrl: String,
    val patch: String? = null,
    @SerialName("previous_filename") val previousFilename: String? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("added")
        Added,
        @SerialName("removed")
        Removed,
        @SerialName("modified")
        Modified,
        @SerialName("renamed")
        Renamed,
        @SerialName("copied")
        Copied,
        @SerialName("changed")
        Changed,
        @SerialName("unchanged")
        Unchanged;
    }
}

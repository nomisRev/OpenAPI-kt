package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Snapshot(
    val version: Long,
    val job: Job,
    val sha: String,
    val ref: String,
    val detector: Detector,
    val metadata: Metadata? = null,
    val manifests: List<Manifest>? = null,
    val scanned: LocalDateTime,
) {
    @Serializable
    data class Job(val id: String, val correlator: String, @SerialName("html_url") val htmlUrl: String? = null)

    @Serializable
    data class Detector(val name: String, val version: String, val url: String)
}

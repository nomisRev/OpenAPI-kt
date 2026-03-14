package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PorterLargeFile(
    @SerialName("ref_name") val refName: String,
    val path: String,
    val oid: String,
    val size: Long,
)

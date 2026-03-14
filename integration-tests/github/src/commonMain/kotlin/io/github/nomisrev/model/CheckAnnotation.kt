package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CheckAnnotation(
    val path: String,
    @SerialName("start_line") val startLine: Long,
    @SerialName("end_line") val endLine: Long,
    @SerialName("start_column") val startColumn: Long?,
    @SerialName("end_column") val endColumn: Long?,
    @SerialName("annotation_level") val annotationLevel: String?,
    val title: String?,
    val message: String?,
    @SerialName("raw_details") val rawDetails: String?,
    @SerialName("blob_href") val blobHref: String,
)

package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningSarifsStatusResponse(
    @SerialName("processing_status") val processingStatus: ProcessingStatus? = null,
    @SerialName("analyses_url") val analysesUrl: String? = null,
    val errors: List<String>? = null,
) {
    @Serializable
    enum class ProcessingStatus {
        @SerialName("pending") Pending, @SerialName("complete") Complete, @SerialName("failed") Failed;
    }
}

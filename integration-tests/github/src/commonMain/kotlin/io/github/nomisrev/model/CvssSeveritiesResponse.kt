package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CvssSeveritiesResponse(
    @SerialName("cvss_v3") val cvssV3: CvssV3? = null,
    @SerialName("cvss_v4") val cvssV4: CvssV4? = null,
) {
    @Serializable
    data class CvssV3(@SerialName("vector_string") val vectorString: String?, val score: Double?)

    @Serializable
    data class CvssV4(@SerialName("vector_string") val vectorString: String?, val score: Double?)
}

package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CommunityProfile(
    @SerialName("health_percentage") val healthPercentage: Long,
    val description: String?,
    val documentation: String?,
    val files: Files,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
    @SerialName("content_reports_enabled") val contentReportsEnabled: Boolean? = null,
) {
    @Serializable
    data class Files(
        @SerialName("code_of_conduct") val codeOfConduct: NullableCodeOfConductSimple?,
        @SerialName("code_of_conduct_file") val codeOfConductFile: NullableCommunityHealthFile?,
        val license: NullableLicenseSimple?,
        val contributing: NullableCommunityHealthFile?,
        val readme: NullableCommunityHealthFile?,
        @SerialName("issue_template") val issueTemplate: NullableCommunityHealthFile?,
        @SerialName("pull_request_template") val pullRequestTemplate: NullableCommunityHealthFile?,
    )
}

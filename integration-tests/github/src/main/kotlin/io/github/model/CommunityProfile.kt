package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Community Profile
 */
@Serializable
public data class CommunityProfile(
  @SerialName("health_percentage")
  public val healthPercentage: Long,
  public val description: String?,
  public val documentation: String?,
  public val files: Files,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("content_reports_enabled")
  public val contentReportsEnabled: Boolean? = null,
) {
  @Serializable
  public data class Files(
    @SerialName("code_of_conduct")
    public val codeOfConduct: NullableCodeOfConductSimple?,
    @SerialName("code_of_conduct_file")
    public val codeOfConductFile: NullableCommunityHealthFile?,
    public val license: NullableLicenseSimple?,
    public val contributing: NullableCommunityHealthFile?,
    public val readme: NullableCommunityHealthFile?,
    @SerialName("issue_template")
    public val issueTemplate: NullableCommunityHealthFile?,
    @SerialName("pull_request_template")
    public val pullRequestTemplate: NullableCommunityHealthFile?,
  )
}

package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class SecurityAdvisoryCreditTypesRead(
  public val `value`: String,
) {
  @SerialName("analyst")
  Analyst("analyst"),
  @SerialName("finder")
  Finder("finder"),
  @SerialName("reporter")
  Reporter("reporter"),
  @SerialName("coordinator")
  Coordinator("coordinator"),
  @SerialName("remediation_developer")
  RemediationDeveloper("remediation_developer"),
  @SerialName("remediation_reviewer")
  RemediationReviewer("remediation_reviewer"),
  @SerialName("remediation_verifier")
  RemediationVerifier("remediation_verifier"),
  @SerialName("tool")
  Tool("tool"),
  @SerialName("sponsor")
  Sponsor("sponsor"),
  @SerialName("other")
  Other("other"),
  ;
}

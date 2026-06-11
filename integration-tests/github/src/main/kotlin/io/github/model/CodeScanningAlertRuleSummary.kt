package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CodeScanningAlertRuleSummary(
  public val id: String? = null,
  public val name: String? = null,
  public val severity: Severity? = null,
  @SerialName("security_severity_level")
  public val securitySeverityLevel: SecuritySeverityLevel? = null,
  public val description: String? = null,
  @SerialName("full_description")
  public val fullDescription: String? = null,
  public val tags: List<String>? = null,
  public val help: String? = null,
  @SerialName("help_uri")
  public val helpUri: String? = null,
) {
  @Serializable
  public enum class SecuritySeverityLevel(
    public val `value`: String,
  ) {
    @SerialName("low")
    Low("low"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("high")
    High("high"),
    @SerialName("critical")
    Critical("critical"),
    ;
  }

  @Serializable
  public enum class Severity(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("note")
    Note("note"),
    @SerialName("warning")
    Warning("warning"),
    @SerialName("error")
    Error("error"),
    ;
  }
}

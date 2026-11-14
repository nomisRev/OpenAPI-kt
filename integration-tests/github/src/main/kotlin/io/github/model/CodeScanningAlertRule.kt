package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAlertRule(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val severity: Optional<Severity> = Optional.Absent,
  @SerialName("security_severity_level")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val securitySeverityLevel: Optional<SecuritySeverityLevel> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @SerialName("full_description")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fullDescription: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tags: Optional<List<String>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val help: Optional<String> = Optional.Absent,
  @SerialName("help_uri")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val helpUri: Optional<String> = Optional.Absent,
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

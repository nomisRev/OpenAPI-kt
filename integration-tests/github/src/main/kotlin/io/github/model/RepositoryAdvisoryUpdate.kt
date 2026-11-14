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
public data class RepositoryAdvisoryUpdate(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val summary: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @SerialName("cve_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cveId: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val vulnerabilities: List<Vulnerabilities>? = null,
  @SerialName("cwe_ids")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cweIds: Optional<List<String>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val credits: Optional<List<Credits>> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val severity: Optional<Severity> = Optional.Absent,
  @SerialName("cvss_vector_string")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cvssVectorString: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val state: State? = null,
  @SerialName("collaborating_users")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val collaboratingUsers: Optional<List<String>> = Optional.Absent,
  @SerialName("collaborating_teams")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val collaboratingTeams: Optional<List<String>> = Optional.Absent,
) {
  @Serializable
  public data class Credits(
    public val login: String,
    public val type: SecurityAdvisoryCreditTypes,
  )

  @Serializable
  public enum class Severity(
    public val `value`: String,
  ) {
    @SerialName("critical")
    Critical("critical"),
    @SerialName("high")
    High("high"),
    @SerialName("medium")
    Medium("medium"),
    @SerialName("low")
    Low("low"),
    ;
  }

  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("published")
    Published("published"),
    @SerialName("closed")
    Closed("closed"),
    @SerialName("draft")
    Draft("draft"),
    ;
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Vulnerabilities(
    public val `package`: Package,
    @SerialName("vulnerable_version_range")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val vulnerableVersionRange: Optional<String> = Optional.Absent,
    @SerialName("patched_versions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val patchedVersions: Optional<String> = Optional.Absent,
    @SerialName("vulnerable_functions")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val vulnerableFunctions: Optional<List<String>> = Optional.Absent,
  ) {
    /**
     * The name of the package affected by the vulnerability.
     */
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Package(
      public val ecosystem: SecurityAdvisoryEcosystems,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: Optional<String> = Optional.Absent,
    )
  }
}

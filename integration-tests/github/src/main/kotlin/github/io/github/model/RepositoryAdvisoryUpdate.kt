package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RepositoryAdvisoryUpdate(
  public val summary: String? = null,
  public val description: String? = null,
  @SerialName("cve_id")
  public val cveId: String? = null,
  public val vulnerabilities: List<Vulnerabilities>? = null,
  @SerialName("cwe_ids")
  public val cweIds: List<String>? = null,
  public val credits: List<Credits>? = null,
  public val severity: Severity? = null,
  @SerialName("cvss_vector_string")
  public val cvssVectorString: String? = null,
  public val state: State? = null,
  @SerialName("collaborating_users")
  public val collaboratingUsers: List<String>? = null,
  @SerialName("collaborating_teams")
  public val collaboratingTeams: List<String>? = null,
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

  @Serializable
  public data class Vulnerabilities(
    public val `package`: Package,
    @SerialName("vulnerable_version_range")
    public val vulnerableVersionRange: String? = null,
    @SerialName("patched_versions")
    public val patchedVersions: String? = null,
    @SerialName("vulnerable_functions")
    public val vulnerableFunctions: List<String>? = null,
  ) {
    /**
     * The name of the package affected by the vulnerability.
     */
    @Serializable
    public data class Package(
      public val ecosystem: SecurityAdvisoryEcosystems,
      public val name: String? = null,
    )
  }
}

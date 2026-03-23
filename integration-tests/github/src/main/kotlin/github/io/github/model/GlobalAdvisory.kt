package io.github.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub Security Advisory.
 */
@Serializable
public data class GlobalAdvisory(
  @SerialName("ghsa_id")
  public val ghsaId: String,
  @SerialName("cve_id")
  public val cveId: String?,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("repository_advisory_url")
  public val repositoryAdvisoryUrl: String?,
  public val summary: String,
  public val description: String?,
  public val type: Type,
  public val severity: Severity,
  @SerialName("source_code_location")
  public val sourceCodeLocation: String?,
  public val identifiers: List<Identifiers>?,
  public val references: List<String>?,
  @SerialName("published_at")
  public val publishedAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("github_reviewed_at")
  public val githubReviewedAt: Instant?,
  @SerialName("nvd_published_at")
  public val nvdPublishedAt: Instant?,
  @SerialName("withdrawn_at")
  public val withdrawnAt: Instant?,
  public val vulnerabilities: List<Vulnerability>?,
  public val cvss: Cvss?,
  @SerialName("cvss_severities")
  public val cvssSeverities: CvssSeverities? = null,
  public val epss: SecurityAdvisoryEpss? = null,
  public val cwes: List<Cwes>?,
  public val credits: List<Credits>?,
) {
  @Serializable
  public data class Credits(
    public val user: SimpleUser,
    public val type: SecurityAdvisoryCreditTypes,
  )

  @Serializable
  public data class Cvss(
    @SerialName("vector_string")
    public val vectorString: String?,
    public val score: Double?,
  )

  @Serializable
  public data class Cwes(
    @SerialName("cwe_id")
    public val cweId: String,
    public val name: String,
  )

  @Serializable
  public data class Identifiers(
    public val type: Type,
    public val `value`: String,
  ) {
    @Serializable
    public enum class Type {
      CVE,
      GHSA,
    }
  }

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
    @SerialName("unknown")
    Unknown("unknown"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("reviewed")
    Reviewed("reviewed"),
    @SerialName("unreviewed")
    Unreviewed("unreviewed"),
    @SerialName("malware")
    Malware("malware"),
    ;
  }
}

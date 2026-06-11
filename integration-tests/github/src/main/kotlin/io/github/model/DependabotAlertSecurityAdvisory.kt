package io.github.model

import kotlin.Double
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Details for the GitHub Security Advisory.
 */
@Serializable
public data class DependabotAlertSecurityAdvisory(
  @SerialName("ghsa_id")
  public val ghsaId: String,
  @SerialName("cve_id")
  public val cveId: String?,
  public val summary: String,
  public val description: String,
  public val vulnerabilities: List<DependabotAlertSecurityVulnerability>,
  public val severity: Severity,
  public val cvss: Cvss,
  @SerialName("cvss_severities")
  public val cvssSeverities: CvssSeverities? = null,
  public val epss: SecurityAdvisoryEpss? = null,
  public val cwes: List<Cwes>,
  public val identifiers: List<Identifiers>,
  public val references: List<References>,
  @SerialName("published_at")
  public val publishedAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("withdrawn_at")
  public val withdrawnAt: Instant?,
) {
  /**
   * Details for the advisory pertaining to the Common Vulnerability Scoring System.
   */
  @Serializable
  public data class Cvss(
    public val score: Double,
    @SerialName("vector_string")
    public val vectorString: String?,
  )

  /**
   * A CWE weakness assigned to the advisory.
   */
  @Serializable
  public data class Cwes(
    @SerialName("cwe_id")
    public val cweId: String,
    public val name: String,
  )

  /**
   * An advisory identifier.
   */
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

  /**
   * A link to additional advisory information.
   */
  @JvmInline
  @Serializable
  public value class References(
    public val url: String,
  )

  @Serializable
  public enum class Severity(
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
}

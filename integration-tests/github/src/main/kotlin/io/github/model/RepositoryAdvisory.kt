package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A repository security advisory.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryAdvisory(
  @SerialName("ghsa_id")
  public val ghsaId: String,
  @SerialName("cve_id")
  public val cveId: String?,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val summary: String,
  public val description: String?,
  public val severity: Severity?,
  public val author: SimpleUser?,
  public val publisher: SimpleUser?,
  public val identifiers: List<Identifiers>,
  public val state: State,
  @SerialName("created_at")
  public val createdAt: Instant?,
  @SerialName("updated_at")
  public val updatedAt: Instant?,
  @SerialName("published_at")
  public val publishedAt: Instant?,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("withdrawn_at")
  public val withdrawnAt: Instant?,
  public val submission: Submission?,
  public val vulnerabilities: List<RepositoryAdvisoryVulnerability>?,
  public val cvss: Cvss?,
  @SerialName("cvss_severities")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val cvssSeverities: Optional<CvssSeverities> = Optional.Absent,
  public val cwes: List<Cwes>?,
  @SerialName("cwe_ids")
  public val cweIds: List<String>?,
  public val credits: List<Credits>?,
  @SerialName("credits_detailed")
  public val creditsDetailed: List<RepositoryAdvisoryCredit>?,
  @SerialName("collaborating_users")
  public val collaboratingUsers: List<SimpleUser>?,
  @SerialName("collaborating_teams")
  public val collaboratingTeams: List<Team>?,
  @SerialName("private_fork")
  public val privateFork: SimpleRepository?,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Credits(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val login: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val type: SecurityAdvisoryCreditTypes? = null,
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
    @SerialName("withdrawn")
    Withdrawn("withdrawn"),
    @SerialName("draft")
    Draft("draft"),
    @SerialName("triage")
    Triage("triage"),
    ;
  }

  @Serializable
  public data class Submission(
    public val accepted: Boolean,
  )
}

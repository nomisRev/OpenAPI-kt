package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GlobalAdvisoryResponse(
    @SerialName("ghsa_id") val ghsaId: String,
    @SerialName("cve_id") val cveId: String?,
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("repository_advisory_url") val repositoryAdvisoryUrl: String?,
    val summary: String,
    val description: String?,
    val type: Type,
    val severity: Severity,
    @SerialName("source_code_location") val sourceCodeLocation: String?,
    val identifiers: List<Identifiers>?,
    val references: List<String>?,
    @SerialName("published_at") val publishedAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("github_reviewed_at") val githubReviewedAt: LocalDateTime?,
    @SerialName("nvd_published_at") val nvdPublishedAt: LocalDateTime?,
    @SerialName("withdrawn_at") val withdrawnAt: LocalDateTime?,
    val vulnerabilities: List<VulnerabilityResponse>?,
    val cvss: Cvss?,
    @SerialName("cvss_severities") val cvssSeverities: CvssSeveritiesResponse? = null,
    val epss: SecurityAdvisoryEpssResponse? = null,
    val cwes: List<Cwes>?,
    val credits: List<Credits>?,
) {
    @Serializable
    enum class Type {
        @SerialName("reviewed") Reviewed, @SerialName("unreviewed") Unreviewed, @SerialName("malware") Malware;
    }

    @Serializable
    enum class Severity {
        @SerialName("critical")
        Critical,
        @SerialName("high")
        High,
        @SerialName("medium")
        Medium,
        @SerialName("low")
        Low,
        @SerialName("unknown")
        Unknown;
    }

    @Serializable
    data class Identifiers(val type: Type, val value: String) {
        @Serializable
        enum class Type {
            CVE, GHSA;
        }
    }

    @Serializable
    data class Cvss(@SerialName("vector_string") val vectorString: String?, val score: Double?)

    @Serializable
    data class Cwes(@SerialName("cwe_id") val cweId: String, val name: String)

    @Serializable
    data class Credits(val user: SimpleUser, val type: SecurityAdvisoryCreditTypes)
}

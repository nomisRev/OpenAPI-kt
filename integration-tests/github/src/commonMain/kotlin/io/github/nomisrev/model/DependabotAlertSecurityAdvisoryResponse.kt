package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class DependabotAlertSecurityAdvisoryResponse(
    @SerialName("ghsa_id") val ghsaId: String,
    @SerialName("cve_id") val cveId: String?,
    val summary: String,
    val description: String,
    val vulnerabilities: List<DependabotAlertSecurityVulnerabilityResponse>,
    val severity: Severity,
    val cvss: Cvss,
    @SerialName("cvss_severities") val cvssSeverities: CvssSeveritiesResponse? = null,
    val epss: SecurityAdvisoryEpssResponse? = null,
    val cwes: List<Cwes>,
    val identifiers: List<Identifiers>,
    val references: List<References>,
    @SerialName("published_at") val publishedAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("withdrawn_at") val withdrawnAt: LocalDateTime?,
) {
    @Serializable
    enum class Severity {
        @SerialName("low") Low, @SerialName("medium") Medium, @SerialName("high") High, @SerialName("critical") Critical;
    }

    @Serializable
    data class Cvss(val score: Double, @SerialName("vector_string") val vectorString: String?)

    @Serializable
    data class Cwes(@SerialName("cwe_id") val cweId: String, val name: String)

    @Serializable
    data class Identifiers(val type: Type, val value: String) {
        @Serializable
        enum class Type {
            CVE, GHSA;
        }
    }

    @Serializable
    @JvmInline
    value class References(val url: String)
}

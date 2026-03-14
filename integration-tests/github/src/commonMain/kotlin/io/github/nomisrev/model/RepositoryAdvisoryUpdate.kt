package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryAdvisoryUpdate(
    val summary: String? = null,
    val description: String? = null,
    @SerialName("cve_id") val cveId: String? = null,
    val vulnerabilities: List<Vulnerabilities>? = null,
    @SerialName("cwe_ids") val cweIds: List<String>? = null,
    val credits: List<Credits>? = null,
    val severity: Severity? = null,
    @SerialName("cvss_vector_string") val cvssVectorString: String? = null,
    val state: State? = null,
    @SerialName("collaborating_users") val collaboratingUsers: List<String>? = null,
    @SerialName("collaborating_teams") val collaboratingTeams: List<String>? = null,
) {
    @Serializable
    data class Vulnerabilities(
        @SerialName("package") val `package`: Package,
        @SerialName("vulnerable_version_range") val vulnerableVersionRange: String? = null,
        @SerialName("patched_versions") val patchedVersions: String? = null,
        @SerialName("vulnerable_functions") val vulnerableFunctions: List<String>? = null,
    ) {
        @Serializable
        data class Package(val ecosystem: SecurityAdvisoryEcosystems, val name: String? = null)
    }

    @Serializable
    data class Credits(val login: String, val type: SecurityAdvisoryCreditTypes)

    @Serializable
    enum class Severity {
        @SerialName("critical") Critical, @SerialName("high") High, @SerialName("medium") Medium, @SerialName("low") Low;
    }

    @Serializable
    enum class State {
        @SerialName("published") Published, @SerialName("closed") Closed, @SerialName("draft") Draft;
    }
}

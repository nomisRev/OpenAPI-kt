package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RepositoryAdvisoryCreate(
    val summary: String,
    val description: String,
    @SerialName("cve_id") val cveId: String? = null,
    val vulnerabilities: List<Vulnerabilities>,
    @SerialName("cwe_ids") val cweIds: List<String>? = null,
    val credits: List<Credits>? = null,
    val severity: Severity? = null,
    @SerialName("cvss_vector_string") val cvssVectorString: String? = null,
    @SerialName("start_private_fork") val startPrivateFork: Boolean? = null,
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
}

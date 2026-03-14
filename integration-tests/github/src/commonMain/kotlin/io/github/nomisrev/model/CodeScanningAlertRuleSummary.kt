package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningAlertRuleSummary(
    val id: String? = null,
    val name: String? = null,
    val severity: Severity? = null,
    @SerialName("security_severity_level") val securitySeverityLevel: SecuritySeverityLevel? = null,
    val description: String? = null,
    @SerialName("full_description") val fullDescription: String? = null,
    val tags: List<String>? = null,
    val help: String? = null,
    @SerialName("help_uri") val helpUri: String? = null,
) {
    @Serializable
    enum class Severity {
        @SerialName("none") None, @SerialName("note") Note, @SerialName("warning") Warning, @SerialName("error") Error;
    }

    @Serializable
    enum class SecuritySeverityLevel {
        @SerialName("low") Low, @SerialName("medium") Medium, @SerialName("high") High, @SerialName("critical") Critical;
    }
}

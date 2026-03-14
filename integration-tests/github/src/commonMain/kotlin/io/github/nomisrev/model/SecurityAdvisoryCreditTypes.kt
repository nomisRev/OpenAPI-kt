package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class SecurityAdvisoryCreditTypes {
    @SerialName("analyst")
    Analyst,
    @SerialName("finder")
    Finder,
    @SerialName("reporter")
    Reporter,
    @SerialName("coordinator")
    Coordinator,
    @SerialName("remediation_developer")
    RemediationDeveloper,
    @SerialName("remediation_reviewer")
    RemediationReviewer,
    @SerialName("remediation_verifier")
    RemediationVerifier,
    @SerialName("tool")
    Tool,
    @SerialName("sponsor")
    Sponsor,
    @SerialName("other")
    Other;
}

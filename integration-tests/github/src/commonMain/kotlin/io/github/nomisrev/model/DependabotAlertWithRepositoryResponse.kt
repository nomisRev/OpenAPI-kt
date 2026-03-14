package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DependabotAlertWithRepositoryResponse(
    val number: AlertNumberResponse,
    val state: State,
    val dependency: Dependency,
    @SerialName("security_advisory") val securityAdvisory: DependabotAlertSecurityAdvisoryResponse,
    @SerialName("security_vulnerability") val securityVulnerability: DependabotAlertSecurityVulnerabilityResponse,
    val url: AlertUrlResponse,
    @SerialName("html_url") val htmlUrl: AlertHtmlUrlResponse,
    @SerialName("created_at") val createdAt: AlertCreatedAtResponse,
    @SerialName("updated_at") val updatedAt: AlertUpdatedAtResponse,
    @SerialName("dismissed_at") val dismissedAt: AlertDismissedAtResponse?,
    @SerialName("dismissed_by") val dismissedBy: NullableSimpleUser?,
    @SerialName("dismissed_reason") val dismissedReason: DismissedReason?,
    @SerialName("dismissed_comment") val dismissedComment: String?,
    @SerialName("fixed_at") val fixedAt: AlertFixedAtResponse?,
    @SerialName("auto_dismissed_at") val autoDismissedAt: AlertAutoDismissedAtResponse? = null,
    @SerialName("dismissal_request") val dismissalRequest: DependabotAlertDismissalRequestSimple? = null,
    val assignees: List<SimpleUser>? = null,
    val repository: SimpleRepository,
) {
    @Serializable
    enum class State {
        @SerialName("auto_dismissed")
        AutoDismissed,
        @SerialName("dismissed")
        Dismissed,
        @SerialName("fixed")
        Fixed,
        @SerialName("open")
        Open;
    }

    @Serializable
    data class Dependency(
        @SerialName("package") val `package`: DependabotAlertPackageResponse? = null,
        @SerialName("manifest_path") val manifestPath: String? = null,
        val scope: Scope? = null,
        val relationship: Relationship? = null,
    ) {
        @Serializable
        enum class Scope {
            @SerialName("development") Development, @SerialName("runtime") Runtime;
        }

        @Serializable
        enum class Relationship {
            @SerialName("unknown") Unknown, @SerialName("direct") Direct, @SerialName("transitive") Transitive;
        }
    }

    @Serializable
    enum class DismissedReason {
        @SerialName("fix_started")
        FixStarted,
        @SerialName("inaccurate")
        Inaccurate,
        @SerialName("no_bandwidth")
        NoBandwidth,
        @SerialName("not_used")
        NotUsed,
        @SerialName("tolerable_risk")
        TolerableRisk;
    }
}

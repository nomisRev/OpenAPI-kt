package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class DependencyGraphDiff(val items: List<Item>) {
    @Serializable
    data class Item(
        @SerialName("change_type") val changeType: ChangeType,
        val manifest: String,
        val ecosystem: String,
        val name: String,
        val version: String,
        @SerialName("package_url") val packageUrl: String?,
        val license: String?,
        @SerialName("source_repository_url") val sourceRepositoryUrl: String?,
        val vulnerabilities: List<Vulnerabilities>,
        val scope: Scope,
    ) {
        @Serializable
        enum class ChangeType {
            @SerialName("added") Added, @SerialName("removed") Removed;
        }

        @Serializable
        data class Vulnerabilities(
            val severity: String,
            @SerialName("advisory_ghsa_id") val advisoryGhsaId: String,
            @SerialName("advisory_summary") val advisorySummary: String,
            @SerialName("advisory_url") val advisoryUrl: String,
        )

        @Serializable
        enum class Scope {
            @SerialName("unknown") Unknown, @SerialName("runtime") Runtime, @SerialName("development") Development;
        }
    }
}

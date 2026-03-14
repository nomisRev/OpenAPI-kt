package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class RepositoryRuleViolationError(
    val message: String? = null,
    @SerialName("documentation_url") val documentationUrl: String? = null,
    val status: String? = null,
    val metadata: Metadata? = null,
) {
    @Serializable
    @JvmInline
    value class Metadata(@SerialName("secret_scanning") val secretScanning: SecretScanning? = null) {
        @Serializable
        @JvmInline
        value class SecretScanning(@SerialName("bypass_placeholders") val bypassPlaceholders: List<BypassPlaceholders>? = null) {
            @Serializable
            data class BypassPlaceholders(
                @SerialName("placeholder_id") val placeholderId: SecretScanningPushProtectionBypassPlaceholderId? = null,
                @SerialName("token_type") val tokenType: String? = null,
            )
        }
    }
}

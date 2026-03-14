package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SecretScanningPatternConfiguration(
    @SerialName("pattern_config_version") val patternConfigVersion: SecretScanningRowVersion? = null,
    @SerialName("provider_pattern_overrides") val providerPatternOverrides: List<SecretScanningPatternOverride>? = null,
    @SerialName("custom_pattern_overrides") val customPatternOverrides: List<SecretScanningPatternOverride>? = null,
)

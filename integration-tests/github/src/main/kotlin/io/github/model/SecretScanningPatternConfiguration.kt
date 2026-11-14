package io.github.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A collection of secret scanning patterns and their settings related to push protection.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SecretScanningPatternConfiguration(
  @SerialName("pattern_config_version")
  public val patternConfigVersion: SecretScanningRowVersion? = null,
  @SerialName("provider_pattern_overrides")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val providerPatternOverrides: List<SecretScanningPatternOverride>? = null,
  @SerialName("custom_pattern_overrides")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val customPatternOverrides: List<SecretScanningPatternOverride>? = null,
)

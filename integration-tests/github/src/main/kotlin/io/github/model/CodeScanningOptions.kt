package io.github.model

import kotlin.Boolean
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Security Configuration feature options for code scanning
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningOptions(
  @SerialName("allow_advanced")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowAdvanced: Optional<Boolean> = Optional.Absent,
)

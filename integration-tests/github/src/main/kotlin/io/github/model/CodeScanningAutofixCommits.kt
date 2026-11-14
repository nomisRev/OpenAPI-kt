package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Commit an autofix for a code scanning alert
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAutofixCommits(
  @SerialName("target_ref")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val targetRef: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: String? = null,
)

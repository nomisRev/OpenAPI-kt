package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * A workflow referenced/reused by the initial caller workflow
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ReferencedWorkflow(
  public val path: String,
  public val sha: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ref: String? = null,
)

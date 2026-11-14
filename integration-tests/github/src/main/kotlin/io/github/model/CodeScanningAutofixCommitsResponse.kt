package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningAutofixCommitsResponse(
  @SerialName("target_ref")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val targetRef: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val sha: String? = null,
)

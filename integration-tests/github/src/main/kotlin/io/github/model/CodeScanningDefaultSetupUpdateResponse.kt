package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * You can use `run_url` to track the status of the run. This includes a property status and conclusion.
 * You should not rely on this always being an actions workflow run object.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CodeScanningDefaultSetupUpdateResponse(
  @SerialName("run_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runId: Long? = null,
  @SerialName("run_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runUrl: String? = null,
)

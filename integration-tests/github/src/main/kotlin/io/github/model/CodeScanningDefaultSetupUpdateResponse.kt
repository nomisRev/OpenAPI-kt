package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * You can use `run_url` to track the status of the run. This includes a property status and conclusion.
 * You should not rely on this always being an actions workflow run object.
 */
@Serializable
public data class CodeScanningDefaultSetupUpdateResponse(
  @SerialName("run_id")
  public val runId: Long? = null,
  @SerialName("run_url")
  public val runUrl: String? = null,
)

package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A self hosted runner
 */
@Serializable
public data class Runner(
  public val id: Long,
  @SerialName("runner_group_id")
  public val runnerGroupId: Long? = null,
  public val name: String,
  public val os: String,
  public val status: String,
  public val busy: Boolean,
  public val labels: List<RunnerLabel>,
  public val ephemeral: Boolean? = null,
)

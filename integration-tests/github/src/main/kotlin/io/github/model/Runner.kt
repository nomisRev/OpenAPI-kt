package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A self hosted runner
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Runner(
  public val id: Long,
  @SerialName("runner_group_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val runnerGroupId: Long? = null,
  public val name: String,
  public val os: String,
  public val status: String,
  public val busy: Boolean,
  public val labels: List<RunnerLabel>,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val ephemeral: Boolean? = null,
)

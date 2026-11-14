package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Required status check
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RepositoryRuleParamsStatusCheckConfiguration(
  public val context: String,
  @SerialName("integration_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val integrationId: Long? = null,
)

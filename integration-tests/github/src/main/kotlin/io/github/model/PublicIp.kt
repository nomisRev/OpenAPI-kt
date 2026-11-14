package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

/**
 * Provides details of Public IP for a GitHub-hosted larger runners
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class PublicIp(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val prefix: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val length: Long? = null,
)

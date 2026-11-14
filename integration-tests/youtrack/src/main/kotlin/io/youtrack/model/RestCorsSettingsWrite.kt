package io.youtrack.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class RestCorsSettingsWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowedOrigins: List<String>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val allowAllOrigins: Boolean? = null,
)

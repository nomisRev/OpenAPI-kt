package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LicenseWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val username: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val license: String? = null,
)

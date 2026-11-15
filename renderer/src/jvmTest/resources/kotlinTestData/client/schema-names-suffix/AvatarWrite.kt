package io.github.nomisrev.render.test.client.schema.names.suffix

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AvatarWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val secret: String? = null,
)

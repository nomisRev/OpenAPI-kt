package io.github.nomisrev.render.test.client.schema.names.suffix

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AvatarRead2(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
)

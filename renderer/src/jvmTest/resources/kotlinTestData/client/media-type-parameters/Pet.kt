package io.github.nomisrev.render.test.client.media.type.parameters

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Pet(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tag: String? = null,
)

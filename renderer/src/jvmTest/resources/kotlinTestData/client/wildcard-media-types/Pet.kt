package io.github.nomisrev.render.test.client.wildcard.media.types

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

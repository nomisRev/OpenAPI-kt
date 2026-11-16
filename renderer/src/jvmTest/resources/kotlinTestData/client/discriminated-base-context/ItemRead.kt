package io.github.nomisrev.render.test.client.discriminated.base.context

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ItemRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  public val name: String,
)

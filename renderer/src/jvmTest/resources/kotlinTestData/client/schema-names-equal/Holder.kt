package io.github.nomisrev.render.test.client.schema.names.equal

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Holder(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val a: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val b: String? = null,
)

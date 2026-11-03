package io.github.nomisrev.render.test.client.allof.required

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Required(
  public val a: String,
  public val b: String?,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val c: String? = null,
)

package io.github.nomisrev.render.test.client.reference.siblings

import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Amount(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `value`: Long? = null,
)

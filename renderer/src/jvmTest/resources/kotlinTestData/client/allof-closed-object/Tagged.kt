package io.github.nomisrev.render.test.client.allof.closed.object_

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Tagged(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tag: String? = null,
)

package io.github.nomisrev.render.test.client.allof.`property`.merge

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class NullableOwner(
  public val id: Long,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
)

package io.github.nomisrev.render.test.client.optional.nullable.wire

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Filter(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val q: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val limit: Int? = null,
)

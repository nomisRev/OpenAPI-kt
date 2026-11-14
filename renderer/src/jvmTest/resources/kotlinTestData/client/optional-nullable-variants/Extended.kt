package io.github.nomisrev.render.test.client.optional.nullable.variants

import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Extended(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val note: Optional<String> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val limit: Optional<Long> = Optional.Absent,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val extra: Optional<Int> = Optional.Absent,
)

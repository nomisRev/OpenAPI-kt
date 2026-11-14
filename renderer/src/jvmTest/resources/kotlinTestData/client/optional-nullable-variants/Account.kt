package io.github.nomisrev.render.test.client.optional.nullable.variants

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Account(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val label: Optional<String> = Optional.Absent,
)

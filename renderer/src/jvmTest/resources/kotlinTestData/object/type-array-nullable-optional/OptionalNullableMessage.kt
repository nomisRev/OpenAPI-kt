package io.github.nomisrev.render.test.object_.type.array.nullable.optional

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OptionalNullableMessage(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: Optional<String> = Optional.Absent,
)

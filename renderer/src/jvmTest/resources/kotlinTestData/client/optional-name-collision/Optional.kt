package io.github.nomisrev.render.test.client.optional.name.collision

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Optional(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `value`: OptionalProperty<String> = OptionalProperty.Absent,
)

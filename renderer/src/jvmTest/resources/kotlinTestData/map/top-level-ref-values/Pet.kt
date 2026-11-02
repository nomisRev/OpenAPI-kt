package io.github.nomisrev.render.test.map.top.level.ref.values

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Pet(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val age: Int? = null,
)

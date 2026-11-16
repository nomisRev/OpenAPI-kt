package io.github.nomisrev.render.test.client.recursive.allof.ref

import kotlin.Int
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Owner(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: Int? = null,
)

package io.github.nomisrev.render.test.union.name.collisions

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Contact(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val email: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val phone: String? = null,
)

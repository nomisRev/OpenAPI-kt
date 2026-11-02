package io.github.nomisrev.render.test.object_.dataclass

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class User(
  public val id: Long,
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val active: Boolean? = null,
)

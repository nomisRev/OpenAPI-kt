package io.github.nomisrev.render.test.client.allof.member.nullable

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class MixedOwner(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val extra: String? = null,
)

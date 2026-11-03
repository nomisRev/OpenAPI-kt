package io.github.nomisrev.render.test.client.component.ref.alias

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Owner(
  public val name: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val nickname: String? = null,
)

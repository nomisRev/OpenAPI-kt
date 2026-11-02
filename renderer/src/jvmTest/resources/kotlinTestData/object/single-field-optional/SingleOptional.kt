package io.github.nomisrev.render.test.object_.single.`field`.optional

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class SingleOptional(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val message: String? = null,
)

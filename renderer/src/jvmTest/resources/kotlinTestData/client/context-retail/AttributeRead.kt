package io.github.nomisrev.render.test.client.context.retail

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AttributeRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val key: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inUse: Boolean? = null,
)

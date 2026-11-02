package io.github.nomisrev.render.test.object_.single.writable.`field`.variants

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocaleDescriptorWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
)

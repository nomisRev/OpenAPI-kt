package io.github.nomisrev.render.test.object_.single.writable.`field`.variants

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class LocaleDescriptorRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val locale: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val language: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val community: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val name: String? = null,
)

package io.github.nomisrev.render.test.object_.single.writable.`field`.variants

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class LocaleDescriptorRead(
  public val id: String? = null,
  public val locale: String? = null,
  public val language: String? = null,
  public val community: Boolean? = null,
  public val name: String? = null,
)

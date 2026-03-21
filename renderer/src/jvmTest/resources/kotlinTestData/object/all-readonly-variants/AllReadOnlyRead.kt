package io.github.nomisrev.render.test.object_.all.readonly.variants

import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class AllReadOnlyRead(
  public val id: String? = null,
  public val presentation: String? = null,
  public val pattern: String? = null,
)

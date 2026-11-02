package io.github.nomisrev.render.test.object_.all.readonly.variants

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AllReadOnlyRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val presentation: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pattern: String? = null,
)

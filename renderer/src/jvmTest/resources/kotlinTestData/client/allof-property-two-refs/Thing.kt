package io.github.nomisrev.render.test.client.allof.`property`.two.refs

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class Thing(
  public val id: String,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val product: ProductVoice? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val level: Level? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val links: Links? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Links(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val rename: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val configure: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val ignore: String? = null,
  )
}
